package com.george.keyControll.view;

import com.george.keyControll.Main;
import com.george.keyControll.model.Person;
import com.george.keyControll.utils.TextValidator;
import com.george.keyControll.viewModel.PersonViewModel;
import org.w3c.dom.Text;

import javax.swing.*;

public class AddEditPersonView {
    public JPanel addEditPersonPanel;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField uidTextField;
    private JTextField namePersonTextField;
    private JTextField cabinetTextField;
    private JTextField imageTextField;
    private JButton scanUidButton;

    private final TextValidator textValidator = new TextValidator();

    private final PersonViewModel personViewModel = new PersonViewModel();

    public AddEditPersonView(Person person) {

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
            Main.startPersonsView();
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

    private boolean validateFields(String uid, String name, String cabinet, String image) {
        return textValidator.isEmptyField(uid) || textValidator.isEmptyField(name)
                || textValidator.isEmptyField(cabinet) || textValidator.isEmptyField(image);
    }
    private Person getPerson() {
        String uid = uidTextField.getText();
        String namePerson = namePersonTextField.getText();
        String cabinet = cabinetTextField.getText();
        String imagePerson = imageTextField.getText();
        if(validateFields(uid, namePerson, cabinet, imagePerson)) {
            return null;
        }

        return new Person(uid, namePerson, imagePerson, cabinet);
    }

    private void setInfoPerson(Person person) {
        uidTextField.setText(person.getUid());
        namePersonTextField.setText(person.getName());
        cabinetTextField.setText(person.getCabinet());
        imageTextField.setText(person.getImage());
    }

    private void createPerson(Person person) {
        personViewModel.createPerson(person);
    }

    private void updatePerson(Person person) {
        personViewModel.updatePerson(person);
    }

}
