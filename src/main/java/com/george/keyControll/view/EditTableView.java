package com.george.keyControll.view;

import com.george.keyControll.Main;
import com.george.keyControll.model.Info;
import com.george.keyControll.viewModel.InfoViewModel;
import gnu.io.NRSerialPort;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;

public class EditTableView {
    private JTextField nameTextField;
    private JTextField cabinetTextField;
    private JTextField dateTextField;
    private JTextField timeTakeTextField;
    private JTextField timeReturnTextField;
    private JButton saveButton;
    private JButton cancelButton;
    public JPanel editTablePanel;
    private JTextField uidPersonTextField;
    private JButton scanUidPersonButton;
    private JTextField uidKeyTextField;
    private JButton scanUidKeyButton;
    private NRSerialPort serial;

    public EditTableView(Info info, int id) {

        int baudRate = 9600;
        String port = "no connection";
        for (String s : NRSerialPort.getAvailableSerialPorts()) {
            System.out.println("Available port: " + s);
            port = s;
        }

        if (port.equals("no connection")) {
            JOptionPane.showMessageDialog(editTablePanel,
                    "Сканер не найден. Попробуйте подключить сканер в другой разъем." +
                            "\nФункционал программы будет ограничен.",
                    "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            serial = new NRSerialPort(port, baudRate);
            serial.connect();

            scanUidPersonButton.addActionListener(e -> scanCard(serial, uidPersonTextField));
            scanUidKeyButton.addActionListener(e -> scanCard(serial, uidKeyTextField));
        }

        nameTextField.setText(info.getPersonName());
        cabinetTextField.setText(info.getCabinet());
        dateTextField.setText(info.getDateTake());
        timeTakeTextField.setText(info.getTimeTake());
        timeReturnTextField.setText(info.getTimeReturn());
        uidPersonTextField.setText(info.getPersonUid());
        uidKeyTextField.setText(info.getCabinetUid());

        saveButton.addActionListener(e -> {
            Info updateInfo = new Info(nameTextField.getText(),
                    uidPersonTextField.getText(),
                    cabinetTextField.getText(),
                    uidKeyTextField.getText(),
                    dateTextField.getText(),
                    timeTakeTextField.getText(),
                    timeReturnTextField.getText());
            new InfoViewModel().updateInfo(updateInfo, id);

            startMainView(serial);
        });

        cancelButton.addActionListener(e -> startMainView(serial));
    }

    private void scanCard(NRSerialPort serial, JTextField textField) {
        DataInputStream ins = new DataInputStream(serial.getInputStream());
        boolean scan = true;

        String uid;

        try {
            while (scan) {
                if (ins.available() > 0) {
                    uid = ins.readLine();
                    if (uid.length() != 0) {
                        scan = false;
                        System.out.println("Uid: " + uid);
                        textField.setText(uid);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void startMainView(NRSerialPort serial) {
        serial.disconnect();
        Main.closeEditTable();
        Main.startMainView();
    }

}
