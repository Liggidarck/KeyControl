package com.george.keyControll.view.settings;

import com.george.keyControll.Main;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;
import com.george.keyControll.model.table.InfoTableModel;
import com.george.keyControll.model.table.KeyTableModel;
import com.george.keyControll.model.table.PersonTableModel;
import com.george.keyControll.viewModel.KeyViewModel;
import com.george.keyControll.viewModel.PersonViewModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class SettingsView {
    public JPanel settingsPanel;
    private JButton addPersonButton;
    private JComboBox<String> themeComboBox;
    private JButton saveSettingsButton;
    private JButton addKeyButton;
    private JButton homeButton;
    private JTextField pathDBTextField;
    private JTable personTable;
    private JTable keyTable;
    private JButton editPersonButton;
    private JTextField personNameTextField;
    private JButton searchPersonButton;
    private JTextField keyTextField;
    private JButton searchKeyButton;
    private JButton editKeyButton;

    private final Preferences appPreferences;

    public SettingsView() {
        appPreferences = Preferences.userRoot().node("appPreferences");
        setPreferences();

        saveSettingsButton.addActionListener(e -> {
            String theme = (String) themeComboBox.getSelectedItem();
            String path = pathDBTextField.getText();
            appPreferences.put("theme", theme);
            appPreferences.put("databasePath", path);

            JOptionPane.showMessageDialog(settingsPanel,
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

        homeButton.addActionListener(e -> {
            Main.startMainView();
            Main.closeSettingsView();
        });

        KeyViewModel keyViewModel = new KeyViewModel();
        ArrayList<Key> allKeys = keyViewModel.getAllKeys();
        TableModel tableKeyModel = new KeyTableModel(allKeys);
        keyTable.setModel(tableKeyModel);

        editKeyButton.addActionListener(e -> {
            int row = keyTable.getSelectedRow();
            if (row == -1) {
                showErrorTable("Выберите строчку в таблице перед изменением.");
                return;
            }

            Key key = allKeys.get(row);
            key.setId(key.getId());

            Main.startAddEditKeyView(key);
            Main.closeSettingsView();
        });

        searchKeyButton.addActionListener(e -> {
            String keyNumber = keyTextField.getText();
            Key key = keyViewModel.getKeyByNumber(keyNumber);

            if(key == null) {
                showErrorTable("Кабинет не найден!");
                return;
            }

            key.setId(key.getId());
            Main.startAddEditKeyView(key);
            Main.closeSettingsView();
        });

        PersonViewModel personViewModel = new PersonViewModel();
        ArrayList<Person> allPersons = personViewModel.getAllPersons();
        TableModel tablePersonModel = new PersonTableModel(allPersons);
        personTable.setModel(tablePersonModel);

        editPersonButton.addActionListener(e -> {
            int row = personTable.getSelectedRow();

            if (row == -1) {
                showErrorTable("Выберите строчку в таблице перед изменением.");
                return;
            }

            Person updatePerson = allPersons.get(row);
            updatePerson.setId(updatePerson.getId());

            Main.startAddEditPersonView(updatePerson);
            Main.closeSettingsView();
        });

        searchPersonButton.addActionListener(e -> {
            String personName = personNameTextField.getText();
            Person person = personViewModel.getPersonByName(personName);

            if(person == null) {
                showErrorTable("Пользователь не найден!");
                return;
            }

            person.setId(person.getId());

            Main.startAddEditPersonView(person);
            Main.closeSettingsView();

        });

    }

    private void showErrorTable(String message) {
        JOptionPane.showMessageDialog(settingsPanel,
                message,
                "Внимание!",
                JOptionPane.WARNING_MESSAGE);
    }


    private void setPreferences() {
        String theme = appPreferences.get("theme", "Светлая");
        String databasePath = appPreferences.get("databasePath", "C:/database/");
        themeComboBox.setSelectedItem(theme);
        pathDBTextField.setText(databasePath);
    }

}
