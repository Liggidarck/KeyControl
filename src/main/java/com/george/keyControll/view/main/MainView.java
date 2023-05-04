package com.george.keyControll.view.main;

import com.george.keyControll.Main;
import com.george.keyControll.model.Info;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;
import com.george.keyControll.model.table.InfoTableModel;
import com.george.keyControll.utils.TextValidator;
import com.george.keyControll.utils.TimeUtils;
import com.george.keyControll.viewModel.InfoViewModel;
import com.george.keyControll.viewModel.KeyViewModel;
import com.george.keyControll.viewModel.PersonViewModel;
import gnu.io.NRSerialPort;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.george.keyControll.utils.TimeUtils.getDate;
import static com.george.keyControll.utils.TimeUtils.getTime;

public class MainView {
    public JPanel mainPanel;
    private JTable infoTable;
    private JLabel scanLabel, personNameLabel, cabinetLabel, currentDateLabel;
    private JButton settingsButton, confirmDateButton, exitButton, deleteInfoButton,
            updateInfoButton, searchUserButton, filterUserUidButton, filterKeyButton, showAllInfoButton,
            showCurrentDateInfoButton, exelButton;
    private JTextField dateTextField, uidPersonInfoTextField, nameInfoTextField, uidKeyInfoTextField,
            cabinetInfoTextField, dateInfoTextField, timeTakeInfoTextField, timeReturnInfoTextField,
            textFieldIdUser, filterUserUidTextField, filterKeyTextField;
    private JButton searchKeyByNameButton;
    private JButton transferButton;
    private final TextValidator textValidator = new TextValidator();
    private final InfoViewModel infoViewModel = new InfoViewModel();
    private final KeyViewModel keyViewModel = new KeyViewModel();
    private final PersonViewModel personViewModel = new PersonViewModel();
    private List<Info> arrayListInfo;
    private TableModel tableModel;
    private NRSerialPort serial;
    private String port = "no connection";
    private int infoId;

    public MainView() {
        currentDateLabel.setText("Сегодня " + getDate());
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
                return;
            }

            serial = new NRSerialPort(port, baudRate);
            serial.connect();

            while (true) {
                String card = scanUidCard();
                String key = scanUidKey();
                infoBehaviour(card, key);
            }

        });
        scannerThread.start();

        dateTextField.setText(getDate());

        arrayListInfo = infoViewModel.getInfoByDate(getDate());
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
            infoViewModel.updateInfo(updateInfo, infoId);
            updateTable(getDate());
        });

        transferButton.addActionListener(e -> {
            Info currentInfo = infoViewModel.getInfoById(infoId);
            currentInfo.setTimeReturn(getTime());
            infoViewModel.updateInfo(currentInfo, infoId);

            Info newInfo = new Info(nameInfoTextField.getText(),
                    uidPersonInfoTextField.getText(),
                    cabinetInfoTextField.getText(),
                    uidKeyInfoTextField.getText(),
                    dateInfoTextField.getText(),
                    timeTakeInfoTextField.getText(),
                    "no time");
            infoViewModel.createInfo(newInfo);

            updateTable(getDate());
        });

        deleteInfoButton.addActionListener(e -> {
            infoViewModel.deleteInfo(infoId);
            updateTable(getDate());
        });

        confirmDateButton.addActionListener(e -> {
            String date = dateTextField.getText();
            if (validateField(date)) {
                showErrorMessage("Это поле не может быть пустым.", "Ошибка");
                return;
            }

            updateTable(date);
        });

        filterUserUidButton.addActionListener(e -> {
            String uidUser = filterUserUidTextField.getText();
            String dateFilter = dateTextField.getText();

            if (validateField(uidUser)) {
                showErrorMessage("Поле uid пользователя не может быть пустым.", "Ошибка");
                return;
            }

            if (validateField(dateFilter)) {
                showErrorMessage("Поле даты не может быть пустым.", "Ошибка");
                return;
            }

            List<Info> infoList = infoViewModel.getListInfoByUserUid(uidUser, dateFilter);
            updateTable(infoList);

            if (infoList.size() == 0) {
                showErrorMessage("Данные не найдены", "Внимание!");
            }

        });

        filterKeyButton.addActionListener(e -> {
            String date = dateTextField.getText();
            String keyNumber = filterKeyTextField.getText();

            List<Info> infoList = infoViewModel.getListByKeyNumber(keyNumber, date);
            updateTable(infoList);
        });

        showCurrentDateInfoButton.addActionListener(e -> updateTable(getDate()));
        showAllInfoButton.addActionListener(e -> {
            List<Info> infoList = infoViewModel.getAllInfo();
            updateTable(infoList);
        });

        searchKeyByNameButton.addActionListener(e -> {
            String keyNumber = cabinetInfoTextField.getText();
            Key key = keyViewModel.getKeyByNumber(keyNumber);

            if (key == null) {
                showErrorMessage("Такого кабинета не существует", "Ошибка");
                return;
            }

            uidKeyInfoTextField.setText(key.getUid());
        });

        exelButton.addActionListener(e -> {
            exportToExcelFile();
        });

        settingsButton.addActionListener(e -> {
            Main.startSettingsView();
            Main.closeMainView();
            if (!port.equals("no connection"))
                serial.disconnect();
            scannerThread.interrupt();
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

        exitButton.addActionListener(e -> Main.closeApp());
    }

    private void exportToExcelFile() {
        List<Info> infoList = infoViewModel.getAllInfo();

        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        File fileToSave = null;

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
        }

        assert fileToSave != null;
        String path = fileToSave.getAbsolutePath() + ".xls";

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ключи");

        int rowNumber = 0;
        Row initRow = sheet.createRow(rowNumber);
        Cell personNameInit = initRow.createCell(0);
        personNameInit.setCellValue("ФИО пользователя");

        Cell personUidInit = initRow.createCell(1);
        personUidInit.setCellValue("UID Пользователя");

        Cell cabinetInit = initRow.createCell(2);
        cabinetInit.setCellValue("Ключ");

        Cell cabinetUIdInit = initRow.createCell(3);
        cabinetUIdInit.setCellValue("Ключ UID");

        Cell dateTakeInit = initRow.createCell(4);
        dateTakeInit.setCellValue("Дата");

        Cell timeTakeInit = initRow.createCell(5);
        timeTakeInit.setCellValue("Время получения");

        Cell timeReturnInit = initRow.createCell(6);
        timeReturnInit.setCellValue("Время возврата");

        for(Info info: infoList) {
            rowNumber++;
            Row row = sheet.createRow(rowNumber);

            Cell personName = row.createCell(0);
            personName.setCellValue(info.getPersonName());

            Cell personUid = row.createCell(1);
            personUid.setCellValue(info.getPersonUid());

            Cell cabinet = row.createCell(2);
            cabinet.setCellValue(info.getCabinet());

            Cell cabinetUId = row.createCell(3);
            cabinetUId.setCellValue(info.getCabinetUid());

            Cell date = row.createCell(4);
            date.setCellValue(info.getDateTake());

            Cell timeTake = row.createCell(5);
            timeTake.setCellValue(info.getTimeTake());

            Cell timeReturn = row.createCell(6);
            timeReturn.setCellValue(info.getTimeReturn());

        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                mainPanel, message, title,
                JOptionPane.ERROR_MESSAGE
        );
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
            showErrorMessage("Не удальсь найти карту в базе! Попробуйте приложить другую карту",
                    "Ошибка");
            return;
        }

        if (key == null) {
            showErrorMessage("Не удальсь найти ключ в базе! Вы уверены что приложили ключ?",
                    "Ошибка!");
            return;
        }

        Info infoByCard = infoViewModel.getInfoByPersonUid(cardUid, getDate());
        Info infoByKey = infoViewModel.getInfoByKeyUidAndDate(keyUid, getDate());

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
                        getTime()), id
                );

                Key updateAvailable = new Key(key.getUid(), key.getNumber(), "true");
                keyViewModel.updateKey(updateAvailable, key.getId());

                personNameLabel.setText("Пользователь: " + person.getName());
                cabinetLabel.setText("Кабинет: " + key.getNumber() + " СДАН НА ПОСТ ОХРАНЫ.");

                updateTable(getDate());

                return;
            }


            createInfo(person, key);
        }

    }

    private void createInfo(Person person, Key key) {
        Info newInfo = new Info(person.getName(), person.getUid(),
                key.getNumber(), key.getUid(), getDate(),
                getTime(), "no time");
        infoViewModel.createInfo(newInfo);

        Key updateAvailable = new Key(key.getUid(), key.getNumber(), "false");
        keyViewModel.updateKey(updateAvailable, key.getId());

        personNameLabel.setText("Пользователь: " + person.getName());
        cabinetLabel.setText("Кабинет: " + key.getNumber());

        updateTable(getDate());
    }

    private boolean validateField(String date) {
        return textValidator.isEmptyField(date);
    }

    public void updateTable(String currentDay) {
        currentDateLabel.setText("Сегодня " + getDate());
        arrayListInfo.clear();
        arrayListInfo = infoViewModel.getInfoByDate(currentDay);
        tableModel = new InfoTableModel(arrayListInfo);
        infoTable.setModel(tableModel);
    }

    public void updateTable(List<Info> infoList) {
        currentDateLabel.setText("Сегодня " + getDate());
        arrayListInfo.clear();
        arrayListInfo = infoList;
        tableModel = new InfoTableModel(arrayListInfo);
        infoTable.setModel(tableModel);
    }

}