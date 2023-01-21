package com.george.keyControll.view;

import com.george.keyControll.Main;
import com.george.keyControll.model.Info;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;
import com.george.keyControll.viewModel.InfoViewModel;
import com.george.keyControll.viewModel.KeyViewModel;
import com.george.keyControll.viewModel.PersonViewModel;
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

    private final PersonViewModel personViewModel = new PersonViewModel();
    private final KeyViewModel keyViewModel = new KeyViewModel();


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

            scanUidPersonButton.addActionListener(e -> {
                String uid = getUId(serial);

                Person person = personViewModel.getPersonByUid(uid);
                if(person == null) {
                    return;
                }

                uidPersonTextField.setText(uid);
                nameTextField.setText(person.getName());
            });

            scanUidKeyButton.addActionListener(e -> {
                String uid = getUId(serial);

                Key key = keyViewModel.getKeyByUid(uid);
                if(key == null) {
                    return;
                }

                uidKeyTextField.setText(uid);
                cabinetTextField.setText(key.getNumber());
            });
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

    private String getUId(NRSerialPort serial) {
        DataInputStream ins = new DataInputStream(serial.getInputStream());
        String uid;

        try {
            while (true) {
                if (ins.available() > 0) {
                    uid = ins.readLine();
                    if (uid.length() != 0) {
                        System.out.println("Uid: " + uid);
                        return uid;
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
