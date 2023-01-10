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

    private boolean scan;

    public EditTableView(Info info, int id) {

        String port = "";
        for (String s : NRSerialPort.getAvailableSerialPorts()) {
            System.out.println("Available port: " + s);
            port = s;
        }

        int baudRate = 9600;
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        serial.connect();


        nameTextField.setText(info.getPersonName());
        cabinetTextField.setText(info.getCabinet());
        dateTextField.setText(info.getDateTake());
        timeTakeTextField.setText(info.getTimeTake());
        timeReturnTextField.setText(info.getTimeReturn());
        uidPersonTextField.setText(info.getPersonUid());
        uidKeyTextField.setText(info.getCabinetUid());

        scanUidPersonButton.addActionListener(e -> {
            DataInputStream ins = new DataInputStream(serial.getInputStream());
            scan = true;

            String cardUid;

            try {
                while (scan) {
                    if (ins.available() > 0) {
                        cardUid = ins.readLine();
                        if(cardUid.length() != 0) {
                            scan = false;
                            System.out.println("CardUid: " + cardUid);
                            uidPersonTextField.setText(cardUid);
                        }
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        scanUidPersonButton.addActionListener(e -> {
            DataInputStream ins = new DataInputStream(serial.getInputStream());
            scan = true;

            String keyUid;

            try {
                while (scan) {
                    if (ins.available() > 0) {
                        keyUid = ins.readLine();
                        if(keyUid.length() != 0) {
                            scan = false;
                            System.out.println("CardUid: " + keyUid);
                            uidKeyTextField.setText(keyUid);
                        }
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveButton.addActionListener(e -> {
            Info updateInfo = new Info(nameTextField.getText(),
                    uidPersonTextField.getText(),
                    cabinetTextField.getText(),
                    uidKeyTextField.getText(),
                    dateTextField.getText(),
                    timeTakeTextField.getText(),
                    timeReturnTextField.getText());
            new InfoViewModel().updateInfo(updateInfo, id);

            Main.closeEditTable();
            Main.startMainView();
        });

        cancelButton.addActionListener(e -> {
            Main.closeEditTable();
            Main.startMainView();
        });

    }

}
