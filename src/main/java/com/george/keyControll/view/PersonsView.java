package com.george.keyControll.view;

import com.george.keyControll.Main;
import com.george.keyControll.model.Person;
import com.george.keyControll.viewModel.PersonViewModel;

import javax.swing.*;
import java.util.ArrayList;

public class PersonsView {
    public JPanel personsPanel;
    private JList<String> personList;
    private JButton addPersonButton;

    private final PersonViewModel personViewModel = new PersonViewModel();

    public PersonsView() {

        addPersonButton.addActionListener(e -> {
            Main.startAddEditPersonView(null);
            Main.closePersonsView();
        });

        ArrayList<Person> allPersons = personViewModel.getAllPersons();

        ArrayList<String> usersNames = new ArrayList<>();
        ArrayList<Person> listPersons = new ArrayList<>();

        for(Person person : allPersons) {
            usersNames.add(person.getName());
            listPersons.add(person);
        }

        DefaultListModel<String> personListModel = new DefaultListModel<>();
        personListModel.addAll(usersNames);
        personList.setModel(personListModel);

        personList.addListSelectionListener(event -> {
            if (event.getValueIsAdjusting()) {
                int userIndex = personList.getSelectedIndex();
                Person person = listPersons.get(userIndex);
                person.setId(person.getId());

                System.out.println(person.getId());

                Main.startAddEditPersonView(person);
                Main.closePersonsView();
            }
        });

    }

}
