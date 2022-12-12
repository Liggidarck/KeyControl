package com.george.keyControll.view.settings;

import com.george.keyControll.Main;
import com.george.keyControll.model.Person;
import com.george.keyControll.utils.TextValidator;
import com.george.keyControll.viewModel.PersonViewModel;
import gnu.io.NRSerialPort;
import org.w3c.dom.Text;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;

public class AddEditPersonView {
    public JPanel addEditPersonPanel;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField uidTextField;
    private JTextField namePersonTextField;
    private JButton scanUidButton;

    private final TextValidator textValidator = new TextValidator();

    private final PersonViewModel personViewModel = new PersonViewModel();

    private boolean scan;

    public AddEditPersonView(Person person) {

        String port = "";
        for (String s : NRSerialPort.getAvailableSerialPorts()) {
            System.out.println("Available port: " + s);
            port = s;
        }

        int baudRate = 9600;
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        serial.connect();

        scanUidButton.addActionListener(e -> {
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
                            uidTextField.setText(cardUid);
                        }
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveButton.addActionListener(e -> {
            Person newPerson = getPerson();

            if(newPerson == null) {
                JOptionPane.showMessageDialog(addEditPersonPanel,
                        "Поля не могут быть пустыми.",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            if (person == null)
                createPerson(newPerson);
            else {
                int id = person.getId();
                newPerson.setId(id);
                updatePerson(newPerson);
            }

            Main.closeAddEditPersons();
            Main.startSettingsView();
        });

        if (person == null) {
            return;
        }

        setInfoPerson(person);
        deleteButton.setVisible(true);
        deleteButton.addActionListener(e -> {
            int id = person.getId();
            personViewModel.deletePerson(id);
        });

    }

    private boolean validateFields(String uid, String name) {
        return textValidator.isEmptyField(uid) || textValidator.isEmptyField(name);
    }
    private Person getPerson() {
        String uid = uidTextField.getText();
        String namePerson = namePersonTextField.getText();
        if(validateFields(uid, namePerson)) {
            return null;
        }

        return new Person(uid, namePerson);
    }

    private void setInfoPerson(Person person) {
        uidTextField.setText(person.getUid());
        namePersonTextField.setText(person.getName());
    }

    private void createPerson(Person person) {
        personViewModel.createPerson(person);
    }

    private void updatePerson(Person person) {
        personViewModel.updatePerson(person);
    }

}
