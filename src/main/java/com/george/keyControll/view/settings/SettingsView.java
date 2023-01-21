package com.george.keyControll.view.settings;

import com.george.keyControll.Main;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;
import com.george.keyControll.viewModel.KeyViewModel;
import com.george.keyControll.viewModel.PersonViewModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class SettingsView {
    public JPanel personsPanel;
    private JTabbedPane tabbedPane1;
    private JList<String> personList;
    private JButton addPersonButton;
    private JComboBox<String> themeComboBox;
    private JButton saveSettingsButton;
    private JList<String> keyList;
    private JButton addKeyButton;
    private JButton homeButton;
    private JTextField pathDBTextField;

    private final Preferences appPreferences;

    public SettingsView() {
        appPreferences = Preferences.userRoot().node("appPreferences");
        setPreferences();

        saveSettingsButton.addActionListener(e -> {
            String theme = (String) themeComboBox.getSelectedItem();
            String path = pathDBTextField.getText();
            appPreferences.put("theme", theme);
            appPreferences.put("databasePath", path);

            JOptionPane.showMessageDialog(personsPanel,
                    "Внимание! Изменения вступят в силу только после перезапуска.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);

            Main.closeSettingsView();
            Main.startMainView();
        });

        addPersonButton.addActionListener(e -> {
            Main.startAddEditPersonView(null);
            Main.closeSettingsView();
        });

        addKeyButton.addActionListener(e -> {
            Main.startAddEditKeyView(null);
            Main.closeSettingsView();
        });

        KeyViewModel keyViewModel = new KeyViewModel();

        ArrayList<Key> allKeys = keyViewModel.getAllKeys();
        ArrayList<String> nameKeys = new ArrayList<>();
        ArrayList<Key> listKeys = new ArrayList<>();

        for (Key key: allKeys) {
            nameKeys.add(key.getNumber());
            listKeys.add(key);
        }

        DefaultListModel<String> keyListModel = new DefaultListModel<>();

        for(String name: nameKeys) {
            keyListModel.addElement(name);
        }
        keyList.setModel(keyListModel);

        keyList.addListSelectionListener(event -> {
            if (event.getValueIsAdjusting()) {
                int keyIndex = keyList.getSelectedIndex();

                Key key = listKeys.get(keyIndex);
                key.setId(key.getId());

                Main.startAddEditKeyView(key);
                Main.closeSettingsView();
            }
        });

        homeButton.addActionListener(e -> {
            Main.startMainView();
            Main.closeSettingsView();
        });

        initPersonList();

    }

    private void initPersonList() {
        PersonViewModel personViewModel = new PersonViewModel();

        ArrayList<Person> allPersons = personViewModel.getAllPersons();

        ArrayList<String> usersNames = new ArrayList<>();
        ArrayList<Person> listPersons = new ArrayList<>();

        for (Person person : allPersons) {
            usersNames.add(person.getName());
            listPersons.add(person);
        }

        DefaultListModel<String> personListModel = new DefaultListModel<>();

        for(String name: usersNames) {
            personListModel.addElement(name);
        }

        personList.setModel(personListModel);

        personList.addListSelectionListener(event -> {
            if (event.getValueIsAdjusting()) {
                int userIndex = personList.getSelectedIndex();
                Person person = listPersons.get(userIndex);
                person.setId(person.getId());

                System.out.println(person.getId());

                Main.startAddEditPersonView(person);
                Main.closeSettingsView();
            }
        });
    }

    private void setPreferences() {
        String theme = appPreferences.get("theme", "Светлая");
        String databasePath = appPreferences.get("databasePath", "C:/database/");
        themeComboBox.setSelectedItem(theme);
        pathDBTextField.setText(databasePath);
    }
}
