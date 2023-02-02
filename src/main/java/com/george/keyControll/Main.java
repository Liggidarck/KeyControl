package com.george.keyControll;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.george.keyControll.model.Info;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;
import com.george.keyControll.view.main.EditTableView;
import com.george.keyControll.view.main.KeyAvailableView;
import com.george.keyControll.view.main.MainView;
import com.george.keyControll.view.main.transfer.TransferToPersonView;
import com.george.keyControll.view.main.transfer.TransferView;
import com.george.keyControll.view.settings.AddEditKeyView;
import com.george.keyControll.view.settings.AddEditPersonView;
import com.george.keyControll.view.settings.SettingsView;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

public class Main {

    private static JFrame settingsFrame;
    private static JFrame addEditPersonsFrame;
    private static JFrame addEditKeysFrame;
    private static JFrame keyAvailableFrame;
    private static JFrame editTableFrame;
    private static JFrame mainFrame;
    private static JFrame transferFrame;

    private static JFrame transferToPersonFrame;

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
        mainFrame = new JFrame("Главная");
        mainFrame.setContentPane(new MainView().mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void startSettingsView() {
        settingsFrame = new JFrame("Настроки");
        settingsFrame.setContentPane(new SettingsView().settingsPanel);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(400, 500);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setVisible(true);
    }

    public static void startEditTableView(Info info, int id) {
        editTableFrame = new JFrame("Изменить строчку");
        editTableFrame.setContentPane(new EditTableView(info, id).editTablePanel);
        editTableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editTableFrame.setSize(400, 500);
        editTableFrame.setLocationRelativeTo(null);
        editTableFrame.setVisible(true);
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

    public static void startKeyAvailableView() {
        keyAvailableFrame = new JFrame("Доступность");
        keyAvailableFrame.setContentPane(new KeyAvailableView().keyAvailablePanel);
        keyAvailableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        keyAvailableFrame.setSize(600, 400);
        keyAvailableFrame.setLocationRelativeTo(null);
        keyAvailableFrame.setVisible(true);
    }

    public static void startTransferView() {
        transferFrame = new JFrame("Передача ключа");
        transferFrame.setContentPane(new TransferView().transferPanel);
        transferFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        transferFrame.setSize(800, 400);
        transferFrame.setLocationRelativeTo(null);
        transferFrame.setVisible(true);
    }

    public static void startTransferToPersonView(Info info) {
        transferToPersonFrame = new JFrame("Передача ключа");
        transferToPersonFrame.setContentPane(new TransferToPersonView(info).transferPanel);
        transferToPersonFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        transferToPersonFrame.setSize(800, 400);
        transferToPersonFrame.setLocationRelativeTo(null);
        transferToPersonFrame.setVisible(true);
    }

    public static void closeSettingsView() {
        settingsFrame.dispatchEvent(new WindowEvent(settingsFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeTransferView() {
        transferFrame.dispatchEvent(new WindowEvent(transferFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeTransferToPersonView() {
        transferToPersonFrame.dispatchEvent(new WindowEvent(transferToPersonFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeAddEditPersons() {
        addEditPersonsFrame.dispatchEvent(new WindowEvent(addEditPersonsFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeAddEditKey() {
        addEditKeysFrame.dispatchEvent(new WindowEvent(addEditKeysFrame, WindowEvent.WINDOW_CLOSING));

    }

    public static void closeMainView() {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
    }
    public static void closeEditTable() {
        editTableFrame.dispatchEvent(new WindowEvent(editTableFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeApp() {
        System.exit(0);
    }

}