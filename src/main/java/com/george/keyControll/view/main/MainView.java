package com.george.keyControll.view.main;

import com.george.keyControll.Main;
import com.george.keyControll.model.Info;
import com.george.keyControll.model.table.InfoTableModel;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;
import com.george.keyControll.utils.TextValidator;
import com.george.keyControll.utils.TimeUtils;
import com.george.keyControll.viewModel.InfoViewModel;
import com.george.keyControll.viewModel.KeyViewModel;
import com.george.keyControll.viewModel.PersonViewModel;
import gnu.io.NRSerialPort;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainView {
    public JPanel mainPanel;
    private JButton settingsButton;
    private JTable infoTable;
    private JButton keyAvailableButton;
    private JTextField dateTextField;
    private JButton confirmDateButton;
    private JLabel scanLabel;
    private JLabel personNameLabel;
    private JLabel cabinetLabel;
    private JButton transferButton;
    private JButton exitButton;
    private JTable tableTransfer;
    private JLabel scanLabelTransfer;
    private JLabel personNameLabelTransfer;
    private JTextField codPersonTextField;
    private JTextField namePersonTextField;
    private JTextField uidPeronTextField;
    private JTextField keyTextField;
    private JTextField uidKeyTextField;
    private JButton searchPersonButton;
    private JTextField uidPersonInfoTextField;
    private JTextField nameInfoTextField;
    private JTextField uidKeyInfoTextField;
    private JTextField cabinetInfoTextField;
    private JTextField dateInfoTextField;
    private JTextField timeTakeInfoTextField;
    private JTextField timeReturnInfoTextField;
    private JButton updateInfoButton;
    private JTextField textFieldIdUser;
    private JButton searchUserButton;
    private JTextField textFieldIdKey;
    private JButton searchKeyButton;
    private final TimeUtils timeUtils = new TimeUtils();
    private final TextValidator textValidator = new TextValidator();
    private final InfoViewModel infoViewModel = new InfoViewModel();
    private final KeyViewModel keyViewModel = new KeyViewModel();
    private final PersonViewModel personViewModel = new PersonViewModel();

    private ArrayList<Info> arrayListInfo;

    private TableModel tableModel;
    private NRSerialPort serial;
    private String port = "no connection";
    private int infoId;

    public MainView() {
        scanLabel.setText("ИНИЦИАЛИЗАЦИЯ... ПОЖАЛУЙСТА, ПОДОЖДИТЕ");

        Thread scannerThread = new Thread(() -> {
            int baudRate = 9600;

            for (String ports : NRSerialPort.getAvailableSerialPorts()) {
                System.out.println("Available port: " + ports);
                port = ports;
            }

            if (port.equals("no connection")) {
                showErrorMessage("Сканер не найден. Попробуйте подключить сканер в другой разъем." +
                        "\nФункционал программы будет ограничен.", "Ошибка");

                scanLabel.setText("СКАНЕР НЕ ПОДКЛЮЧЕН");
                scanLabelTransfer.setText("СКАНЕР НЕ ПОДКЛЮЧЕН");
            } else {
                serial = new NRSerialPort(port, baudRate);
                serial.connect();

                while (true) {
                    String card = scanUidCard();
                    transferBehaviour(card);
                    String key = scanUidKey();
                    infoBehaviour(card, key);
                }
            }
        });
        scannerThread.start();

        dateTextField.setText(timeUtils.getDate());

        arrayListInfo = infoViewModel.getInfoByDate(timeUtils.getDate());
        tableModel = new InfoTableModel(arrayListInfo);
        infoTable.setModel(tableModel);

        infoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && infoTable.getSelectedRow() != -1) {
                int row = infoTable.getSelectedRow();

                Info updateInfo = arrayListInfo.get(row);
                infoId = updateInfo.getId();

                nameInfoTextField.setText(updateInfo.getPersonName());
                cabinetInfoTextField.setText(updateInfo.getCabinet());
                dateInfoTextField.setText(updateInfo.getDateTake());
                timeTakeInfoTextField.setText(updateInfo.getTimeTake());
                timeReturnInfoTextField.setText(updateInfo.getTimeReturn());
                uidPersonInfoTextField.setText(updateInfo.getPersonUid());
                uidKeyInfoTextField.setText(updateInfo.getCabinetUid());

            }
        });

        updateInfoButton.addActionListener(e -> {
            Info updateInfo = new Info(nameInfoTextField.getText(),
                    uidPersonInfoTextField.getText(),
                    cabinetInfoTextField.getText(),
                    uidKeyInfoTextField.getText(),
                    dateInfoTextField.getText(),
                    timeTakeInfoTextField.getText(),
                    timeReturnInfoTextField.getText());
            new InfoViewModel().updateInfo(updateInfo, infoId);
            updateTable(timeUtils.getDate());
        });

        confirmDateButton.addActionListener(e -> {
            String date = dateTextField.getText();
            if (validateField(date)) {
                showErrorMessage("Это поле не может быть пустым.", "Ошибка");
                return;
            }

            updateTable(date);
        });

        keyAvailableButton.addActionListener(e -> Main.startKeyAvailableView());

        settingsButton.addActionListener(e -> {
            Main.startSettingsView();
            Main.closeMainView();
            if (!port.equals("no connection"))
                serial.disconnect();
            scannerThread.stop();
        });

        searchPersonButton.addActionListener(e -> {
            String personId = codPersonTextField.getText();
            Person person = personViewModel.getPersonById(Integer.parseInt(personId));
            if (person == null) {
                showErrorMessage("Введен некорректный код", "Ошибка");
                return;
            }

            namePersonTextField.setText(person.getName());
            uidPeronTextField.setText(person.getUid());
        });

        searchUserButton.addActionListener(e -> {
            String personId = textFieldIdUser.getText();
            Person person = personViewModel.getPersonById(Integer.parseInt(personId));
            if (person == null) {
                showErrorMessage("Введен некорректный код", "Ошибка");
                return;
            }

            uidPersonInfoTextField.setText(person.getUid());
            nameInfoTextField.setText(person.getName());
        });

        searchKeyButton.addActionListener(e -> {
            String keyId = textFieldIdKey.getText();
            Key key = keyViewModel.getKeyById(Integer.parseInt(keyId));

            if (key == null) {
                showErrorMessage("Введен некорректный код", "Ошибка");
                return;
            }

            uidKeyInfoTextField.setText(key.getUid());
            cabinetInfoTextField.setText(key.getNumber());
        });

        exitButton.addActionListener(e -> Main.closeApp());
    }

    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                mainPanel, message, title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void transferBehaviour(String card) {
        Person person = personViewModel.getPersonByUid(card);

        if (person == null) {

            return;
        }

        List<Info> infoTransferList = infoViewModel.getTransferList(card, timeUtils.getDate());

        if (infoTransferList.size() == 0) {
            scanLabelTransfer.setText("Отсуствуют ключи для передачи");
            personNameLabelTransfer.setText("");
            return;
        }

        if (infoTransferList.size() == 1) {
            scanLabelTransfer.setText("Найден ключ");
            Info transferInfo = infoTransferList.get(0);
            namePersonTextField.setText(transferInfo.getPersonName());
            personNameLabelTransfer.setText(transferInfo.getPersonName() + " " + transferInfo.getCabinet());
            uidPeronTextField.setText(transferInfo.getPersonUid());
            keyTextField.setText(transferInfo.getCabinet());
            uidKeyTextField.setText(transferInfo.getCabinetUid());

            transferButton(transferInfo, transferInfo.getId());
            return;
        }

        scanLabelTransfer.setText("Выберите ключ");
        TableModel tableTransferModel = new InfoTableModel(infoTransferList);
        tableTransfer.setModel(tableTransferModel);
    }

    private void transferButton(Info info, int infoId) {
        transferButton.addActionListener(e -> {
            Info updateInfo = new Info(info.getPersonName(), info.getPersonUid(),
                    info.getCabinet(), info.getCabinetUid(), info.getDateTake(),
                    info.getTimeTake(), timeUtils.getTime()
            );

            infoViewModel.updateInfo(updateInfo, infoId);

            Info createNewInfo = new Info(namePersonTextField.getText(),
                    uidPeronTextField.getText(), info.getCabinet(),
                    info.getCabinetUid(), info.getDateTake(),
                    info.getTimeTake(), "no time"
            );

            infoViewModel.createInfo(createNewInfo);

            updateTable(timeUtils.getDate());
        });
    }

    private String scanUidCard() {
        System.out.println("\nОТСКАНИРУЙТЕ КАРТУ");
        scanLabel.setText("ОТСКАНИРУЙТЕ КАРТУ");
        scanLabelTransfer.setText("ОТСКАНИРУЙТЕ КАРТУ");
        return getUid();
    }

    private String scanUidKey() {
        System.out.println("ОТСКАНИРУЙТЕ КЛЮЧ");
        scanLabel.setText("ОТСКАНИРУЙТЕ КЛЮЧ");

        return getUid();
    }

    private String getUid() {
        DataInputStream ins = new DataInputStream(serial.getInputStream());
        String uid;
        try {
            while (true) {
                if (ins.available() > 0) {
                    uid = ins.readLine();
                    if (uid.length() != 0) {
                        System.out.println("uid" + uid);
                        return uid;
                    }
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void infoBehaviour(String cardUid, String keyUid) {
        Person person = personViewModel.getPersonByUid(cardUid);
        Key key = keyViewModel.getKeyByUid(keyUid);

        if (person == null) {
            showErrorMessage("Не удальсь найти карту в базе! Попробуйте приложить другую карту",
                    "Ошибка");
            return;
        }

        if (key == null) {
            showErrorMessage("Не удальсь найти ключ в базе! Вы уверены что приложили ключ?",
                    "Ошибка!");
            return;
        }

        Info infoByCard = infoViewModel.getInfoByPersonUid(cardUid, timeUtils.getDate());
        Info infoByKey = infoViewModel.getInfoByKeyUidAndDate(keyUid, timeUtils.getDate());

        // Запись в таблице по карте существует?
        if (infoByCard == null) {
            createInfo(person, key);
            return;
        }

        // Запись в таблице по ключу существует?
        if (infoByKey == null) {
            createInfo(person, key);
            return;
        }

        String keyUidInfo = infoByKey.getCabinetUid();

        // Отсканированный ключ совпадает с таблицей?
        if (keyUidInfo.equals(keyUid)) {
            String timeReturn = infoByKey.getTimeReturn();
            int id = infoByKey.getId();

            if (timeReturn.equals("no time")) {
                infoViewModel.updateInfo(new Info(
                        infoByKey.getPersonName(),
                        infoByKey.getPersonUid(),
                        infoByKey.getCabinet(),
                        infoByKey.getCabinetUid(),
                        infoByKey.getDateTake(),
                        infoByKey.getTimeTake(),
                        timeUtils.getTime()), id
                );

                Key updateAvailable = new Key(key.getUid(), key.getNumber(), "true");
                keyViewModel.updateKey(updateAvailable, key.getId());

                personNameLabel.setText("Пользователь: " + person.getName());
                cabinetLabel.setText("Кабинет: " + key.getNumber() + " СДАН НА ПОСТ ОХРАНЫ.");

                updateTable(timeUtils.getDate());

                return;
            }


            createInfo(person, key);
        }

    }

    private void createInfo(Person person, Key key) {
        Info newInfo = new Info(person.getName(), person.getUid(),
                key.getNumber(), key.getUid(), timeUtils.getDate(),
                timeUtils.getTime(), "no time");
        infoViewModel.createInfo(newInfo);

        Key updateAvailable = new Key(key.getUid(), key.getNumber(), "false");
        keyViewModel.updateKey(updateAvailable, key.getId());

        personNameLabel.setText("Пользователь: " + person.getName());
        cabinetLabel.setText("Кабинет: " + key.getNumber());

        updateTable(timeUtils.getDate());
    }

    private boolean validateField(String date) {
        return textValidator.isEmptyField(date);
    }

    public void updateTable(String currentDay) {
        arrayListInfo.clear();
        arrayListInfo = infoViewModel.getInfoByDate(currentDay);
        tableModel = new InfoTableModel(arrayListInfo);
        infoTable.setModel(tableModel);
    }
}