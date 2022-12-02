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
    private JTable keysTable;
    private JLabel personNameLabel;
    private JLabel cabinetPersonLabel;
    private JButton scanCardButton;
    private JButton keyAvailableButton;
    private JTextField dateTextField;
    private JButton confirmDateButton;
    private JLabel welcomeLabel;
    private final TimeUtils timeUtils = new TimeUtils();
    private final TextValidator textValidator = new TextValidator();
    private final InfoViewModel infoViewModel = new InfoViewModel();
    private final KeyViewModel keyViewModel = new KeyViewModel();
    private final PersonViewModel personViewModel = new PersonViewModel();
    private ArrayList<Info> infos;
    private TableModel model;
    private String currentDay;
    private boolean scan;

    public MainView() {

        String port = "";
        for (String s : NRSerialPort.getAvailableSerialPorts()) {
            System.out.println("Available port: " + s);
            port = s;
        }

        int baudRate = 9600;
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        serial.connect();

        currentDay = timeUtils.getDate();
        setWelcomeText();

        infos = infoViewModel.getInfoByDate(currentDay);
        model = new InfoTableModel(infos);
        keysTable.setModel(model);

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

        settingsButton.addActionListener(e -> Main.startSettingsView());

        scanCardButton.addActionListener(e -> {
//            scan = true;
//            String cardUid, keyUid;
//            DataInputStream ins = new DataInputStream(serial.getInputStream());
//
//            try {
//                while (scan) {
//                    if (ins.available() > 0) {
//                        cardUid = ins.readLine();
//                        if(cardUid.length() != 0) {
//                            scan = false;
//                            System.out.println("CardUid: " + cardUid);
//                        }
//                    }
//                }
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//
//            scan = true;
//            try {
//                while (scan) {
//                    if (ins.available() > 0) {
//                        keyUid = ins.readLine();
//                        if(keyUid.length() != 0) {
//                            scan = false;
//                            System.out.println("KeyUid: " + keyUid);
//                        }
//                    }
//                }
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }

            infoBehaviour("4194777190", "1");
        });
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

    private void updateTable(String currentDay) {
        infos.clear();
        infos = infoViewModel.getInfoByDate(currentDay);
        model = new InfoTableModel(infos);
        keysTable.setModel(model);
    }
}
