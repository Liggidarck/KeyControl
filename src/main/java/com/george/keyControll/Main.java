package com.george.keyControll;

import com.formdev.flatlaf.FlatDarkLaf;
import com.george.keyControll.model.Person;
import com.george.keyControll.view.AddEditPersonView;
import com.george.keyControll.view.MainView;
import com.george.keyControll.view.PersonsView;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class Main {

    private static JFrame personsFrame;
    private static JFrame addEditPersonsFrame;

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        startMainView();
    }

    public static void startMainView() {
        JFrame mainFrame = new JFrame("Главная");
        mainFrame.setContentPane(new MainView().mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void startPersonsView() {
        personsFrame = new JFrame("Пользователи");
        personsFrame.setContentPane(new PersonsView().personsPanel);
        personsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        personsFrame.setSize(600, 400);
        personsFrame.setLocationRelativeTo(null);
        personsFrame.setVisible(true);
    }

    public static void startAddEditPersonView(Person person) {
        addEditPersonsFrame = new JFrame("Добавить пользователя");
        addEditPersonsFrame.setContentPane(new AddEditPersonView(person).addEditPersonPanel);
        addEditPersonsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addEditPersonsFrame.setSize(600, 400);
        addEditPersonsFrame.setLocationRelativeTo(null);
        addEditPersonsFrame.setVisible(true);
    }

    public static void closePersonsView() {
        personsFrame.dispatchEvent(new WindowEvent(personsFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void closeAddEditPersons() {
        addEditPersonsFrame.dispatchEvent(new WindowEvent(addEditPersonsFrame, WindowEvent.WINDOW_CLOSING));
    }

}