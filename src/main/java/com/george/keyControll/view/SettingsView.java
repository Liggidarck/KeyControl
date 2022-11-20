package com.george.keyControll.view;

import com.george.keyControll.Main;
import com.george.keyControll.model.Person;
import com.george.keyControll.viewModel.PersonViewModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class SettingsView {
    public JPanel personsPanel;
    private JList<String> personList;
    private JButton addPersonButton;
    private JComboBox<String> themeComboBox;
    private JButton saveSettingsButton;

    private final Preferences appPreferences;

    public SettingsView() {
        appPreferences = Preferences.userRoot().node("appPreferences");
        setPreferences();

        saveSettingsButton.addActionListener(e -> {
            String theme = (String) themeComboBox.getSelectedItem();
            appPreferences.put("theme", theme);

            JOptionPane.showMessageDialog(personsPanel,
                    "Внимание! Изменения вступят в силу только после перезапуска.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
        });

        addPersonButton.addActionListener(e -> {
            Main.startAddEditPersonView(null);
            Main.closePersonsView();
        });

        PersonViewModel personViewModel = new PersonViewModel();
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

    private void setPreferences() {
        String theme = appPreferences.get("theme", "Светлая");
        themeComboBox.setSelectedItem(theme);
    }

}
