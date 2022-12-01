package com.george.keyControll;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;
import com.george.keyControll.view.settings.AddEditKeyView;
import com.george.keyControll.view.settings.AddEditPersonView;
import com.george.keyControll.view.MainView;
import com.george.keyControll.view.settings.SettingsView;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

public class Main {

    private static JFrame settingsFrame;
    private static JFrame addEditPersonsFrame;

    private static JFrame addEditKeysFrame;

    public static void main(String[] args) {
        setUpWithPreferences();
        startMainView();
    }


    private static void setUpWithPreferences() {
        Preferences appPreferences = Preferences.userRoot().node("appPreferences");
        String theme = appPreferences.get("theme", "Светлая");

        if (theme.equals("Темная"))
            FlatDarkLaf.setup();
        if (theme.equals("Светлая"))
            FlatLightLaf.setup();
    }

    public static void startMainView() {
        JFrame mainFrame = new JFrame("Главная");
        mainFrame.setContentPane(new MainView().mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void startSettingsView() {
        settingsFrame = new JFrame("Настроки");
        settingsFrame.setContentPane(new SettingsView().personsPanel);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(600, 400);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setVisible(true);
    }

    public static void startAddEditPersonView(Person person) {
        addEditPersonsFrame = new JFrame("Добавить пользователя");
        addEditPersonsFrame.setContentPane(new AddEditPersonView(person).addEditPersonPanel);
        addEditPersonsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addEditPersonsFrame.setSize(600, 400);
        addEditPersonsFrame.setLocationRelativeTo(null);
        addEditPersonsFrame.setVisible(true);
    }

    public static void startAddEditKeyView(Key key) {
        addEditKeysFrame = new JFrame("Добавить ключ");
        addEditKeysFrame.setContentPane(new AddEditKeyView(key).addEditPanel);
        addEditKeysFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addEditKeysFrame.setSize(600, 400);
        addEditKeysFrame.setLocationRelativeTo(null);
        addEditKeysFrame.setVisible(true);
    }

    public static void closeSettingsView() {
        settingsFrame.dispatchEvent(new WindowEvent(settingsFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeAddEditPersons() {
        addEditPersonsFrame.dispatchEvent(new WindowEvent(addEditPersonsFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeAddEditKey() {
        addEditKeysFrame.dispatchEvent(new WindowEvent(addEditKeysFrame, WindowEvent.WINDOW_CLOSING));

    }

}