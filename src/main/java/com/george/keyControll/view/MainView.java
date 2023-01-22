package com.george.keyControll.view;

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

public class MainView {
    public JPanel mainPanel;
    private JButton settingsButton;
    private JTable keysTableView;
    private JButton keyAvailableButton;
    private JTextField dateTextField;
    private JButton confirmDateButton;
    private JLabel scanLabel;
    private JButton editRowButton;
    private JLabel personNameLabel;
    private JLabel cabinetLabel;
    private final TimeUtils timeUtils = new TimeUtils();
    private final TextValidator textValidator = new TextValidator();
    private final InfoViewModel infoViewModel = new InfoViewModel();
    private final KeyViewModel keyViewModel = new KeyViewModel();
    private final PersonViewModel personViewModel = new PersonViewModel();

    private ArrayList<Info> arrayListInfo;
    private TableModel tableModel;
    private final String currentDay;
    private NRSerialPort serial;
    private String port = "no connection";

    public MainView() {

        Thread scannerThread = new Thread(() -> {
            int baudRate = 9600;

            for (String ports : NRSerialPort.getAvailableSerialPorts()) {
                System.out.println("Available port: " + ports);
                port = ports;
            }

            if (port.equals("no connection")) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Сканер не найден. Попробуйте подключить сканер в другой разъем." +
                                "\nФункционал программы будет ограничен.",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                serial = new NRSerialPort(port, baudRate);
                serial.connect();

                while (true) {
                    String card = scanUidCard();
                    String key = scanUidKey();
                    infoBehaviour(card, key);
                }
            }
        });
        scannerThread.start();

        currentDay = timeUtils.getDate();
        dateTextField.setText(currentDay);

        arrayListInfo = infoViewModel.getInfoByDate(currentDay);
        tableModel = new InfoTableModel(arrayListInfo);
        keysTableView.setModel(tableModel);

        editRowButton.addActionListener(e -> {
            System.out.println(keysTableView.getSelectedRow());
            int row = keysTableView.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Выберите строчку в таблице перед изменением.",
                        "Внимание!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Info updateInfo = arrayListInfo.get(row);
            int id = updateInfo.getId();

            scannerThread.stop();
            if (!port.equals("no connection"))
                serial.disconnect();
            Main.closeMainView();
            Main.startEditTableView(updateInfo, id);
        });

        confirmDateButton.addActionListener(e -> {
            String date = dateTextField.getText();
            if (validateField(date)) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Это поле не может быть пустым.",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
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

    }

    private String scanUidCard() {
        System.out.println("\nОТСКАНИРУЙТЕ КАРТУ");
        scanLabel.setText("ОТСКАНИРУЙТЕ КАРТУ");
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
            JOptionPane.showMessageDialog(mainPanel,
                    "Не удальсь найти карту в базе! Попробуйте приложить другую карту",
                    "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (key == null) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Не удальсь найти ключ в базе! Вы уверены что приложили ключ?",
                    "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Info infoByCard = infoViewModel.getInfoByPersonUid(cardUid, currentDay);
        Info infoByKey = infoViewModel.getInfoByKeyUidAndDate(keyUid, currentDay);

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

                personNameLabel.setText("Работник: " + person.getName());
                cabinetLabel.setText("Кабинет: " + key.getNumber() + " СДАН НА ПОСТ ОХРАНЫ.");

                updateTable(currentDay);
            } else {
                createInfo(person, key);
            }
        }

    }

    private void createInfo(Person person, Key key) {
        Info newInfo = new Info(person.getName(), person.getUid(),
                key.getNumber(), key.getUid(), timeUtils.getDate(),
                timeUtils.getTime(), "no time");
        infoViewModel.createInfo(newInfo);

        Key updateAvailable = new Key(key.getUid(), key.getNumber(), "false");
        keyViewModel.updateKey(updateAvailable, key.getId());

        personNameLabel.setText("Работник: " + person.getName());
        cabinetLabel.setText("Кабинет: " + key.getNumber());

        updateTable(currentDay);
    }

    private boolean validateField(String date) {
        return textValidator.isEmptyField(date);
    }

    public void updateTable(String currentDay) {
        arrayListInfo.clear();
        arrayListInfo = infoViewModel.getInfoByDate(currentDay);
        tableModel = new InfoTableModel(arrayListInfo);
        keysTableView.setModel(tableModel);
    }
}
