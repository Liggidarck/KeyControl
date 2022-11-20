package com.george.keyControll.view;

import com.george.keyControll.Main;

import javax.swing.*;

public class MainView {
    public JPanel mainPanel;
    private JButton settingsButton;
    private JTable keysTable;
    private JButton calendarButton;
    private JLabel personNameLabel;
    private JLabel cabinetPersonLabel;

    public MainView() {
        settingsButton.addActionListener(e -> {
            Main.startPersonsView();
        });
    }

}
