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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainView {
    public JPanel mainPanel;
    private JButton settingsButton;
    private JTable keysTableView;
    private JLabel personNameLabel;
    private JLabel cabinetPersonLabel;
    private JButton scanCardButton;
    private JButton keyAvailableButton;
    private JTextField dateTextField;
    private JButton confirmDateButton;
    private JLabel welcomeLabel;
    private JButton exportDataButton;
    private JButton editRowButton;
    private final TimeUtils timeUtils = new TimeUtils();
    private final TextValidator textValidator = new TextValidator();
    private final InfoViewModel infoViewModel = new InfoViewModel();
    private final KeyViewModel keyViewModel = new KeyViewModel();
    private final PersonViewModel personViewModel = new PersonViewModel();
    private ArrayList<Info> arrayListInfo;
    private TableModel tableModel;
    private final String currentDay;
    private boolean scan;
    private NRSerialPort serial;

    public MainView() {
        int baudRate = 9600;
        String port = "no connection";

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
            scanCardButton.addActionListener(e -> scanUidCard());
        }

        currentDay = timeUtils.getDate();
        setWelcomeText();

        arrayListInfo = infoViewModel.getInfoByDate(currentDay);
        tableModel = new InfoTableModel(arrayListInfo);
        keysTableView.setModel(tableModel);

        String finalPort = port;
        editRowButton.addActionListener(e -> {
            System.out.println(keysTableView.getSelectedRow());
            int row = keysTableView.getSelectedRow();

            if(row == -1) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Выберите строчку в таблице перед изменением.",
                        "Внимание!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Info updateInfo = arrayListInfo.get(row);
            int id = updateInfo.getId();

            if(!finalPort.equals("no connection"))
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
            if(!finalPort.equals("no connection"))
                serial.disconnect();
        });

    }

    private void scanUidCard() {
        scan = true;
        String cardUid = null;
        DataInputStream ins = new DataInputStream(serial.getInputStream());

        try {
            while (scan) {
                if (ins.available() > 0) {
                    cardUid = ins.readLine();
                    if (cardUid.length() != 0) {
                        scan = false;
                        System.out.println("CardUid: " + cardUid);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        scanUidKey(cardUid);
    }

    private void scanUidKey(String cardUid) {
        String keyUid = null;
        DataInputStream ins = new DataInputStream(serial.getInputStream());

        scan = true;
        try {
            while (scan) {
                if (ins.available() > 0) {
                    keyUid = ins.readLine();
                    if (keyUid.length() != 0) {
                        scan = false;
                        System.out.println("KeyUid: " + keyUid);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        infoBehaviour(cardUid, keyUid);
    }

    private void infoBehaviour(String cardUid, String keyUid) {
        Person person = personViewModel.getPersonByUid(cardUid);
        Key key = keyViewModel.getKeyByUid(keyUid);

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
        cabinetPersonLabel.setText("Кабинет: " + key.getNumber());

        updateTable(currentDay);
    }

    private boolean validateField(String date) {
        return textValidator.isEmptyField(date);
    }

    private void setWelcomeText() {
        welcomeLabel.setText("Добро пожаловать, сегодня " +
                currentDay);
    }

    public void updateTable(String currentDay) {
        arrayListInfo.clear();
        arrayListInfo = infoViewModel.getInfoByDate(currentDay);
        tableModel = new InfoTableModel(arrayListInfo);
        keysTableView.setModel(tableModel);
    }
}
