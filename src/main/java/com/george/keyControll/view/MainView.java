package com.george.keyControll.view;

import com.george.keyControll.Main;
import com.george.keyControll.model.Key;
import com.george.keyControll.model.KeyTableModel;
import com.george.keyControll.model.Person;
import com.george.keyControll.utils.TimeUtils;
import com.george.keyControll.viewModel.KeyViewModel;
import com.george.keyControll.viewModel.PersonViewModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class MainView {
    public JPanel mainPanel;
    private JButton settingsButton;
    private JTable keysTable;
    private JLabel personNameLabel;
    private JLabel cabinetPersonLabel;
    private JButton scanCardButton;
    private JButton updateDateButton;
    private JTextField dateTextField;
    private JButton confirmDateButton;
    private JLabel welcomeLabel;

    private final TimeUtils timeUtils = new TimeUtils();
    private final KeyViewModel keyViewModel = new KeyViewModel();

    private final PersonViewModel personViewModel = new PersonViewModel();

    private ArrayList<Key> keys;
    private TableModel model;

    private String currentDay;

    public MainView() {
        settingsButton.addActionListener(e -> Main.startPersonsView());

        updateDateButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel,
                    "Вы успешно изменили дату во всем приложении.",
                    "Внимание!",
                    JOptionPane.ERROR_MESSAGE);

            currentDay = timeUtils.getDate();
            setUpdatedDate();
            updateTable();
        });

        setUpdatedDate();

        currentDay = timeUtils.getDate();
        keys = keyViewModel.getKeyByDate(currentDay);
        model = new KeyTableModel(keys);
        keysTable.setModel(model);

        scanCardButton.addActionListener(e -> {
            // 1. get uid from card. +
            // 2. find user with this uid. After: show name and cabinet
            // 3. check status key dateTake, timeTake
            // 4. if ((dateTake & timeTake == null) || key == null) -> write to table taking key with time.
            // 5. else -> write to table returnTime.
            // 6. updateTable

            String uid = "1";
            Key key = keyViewModel.getKeyByUid(uid);
            Person person = personViewModel.getPersonByUid(uid);

            if (checkEmptyPerson(person)) return;

            String personName = person.getName();
            String cabinet = "Кабинет: " + person.getCabinet();
            personNameLabel.setText(personName);
            cabinetPersonLabel.setText(cabinet);

            if (key == null) {
                saveKey(uid, person, personName, person.getCabinet());
                return;
            }

            String timeReturn = key.getTimeReturn();
            if (timeReturn.equals("no time")) {
                int id = key.getId();
                Key updateKey = new Key(uid, personName, person.getImage(),
                        person.getCabinet(), key.getDateTake(), key.getTimeTake(), timeUtils.getTime());
                updateKey.setId(id);

                keyViewModel.updateKey(updateKey);

                updateTable();

                return;
            }

            saveKey(uid, person, personName, person.getCabinet());


        });

    }

    private void setUpdatedDate() {
        welcomeLabel.setText("Добропожаловать, сегодня " +
                timeUtils.getDate());
    }

    private boolean checkEmptyPerson(Person person) {
        if(person == null) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Нет ни одного зарегестрированного пользователя.",
                    "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);

            return true;
        }
        return false;
    }

    private void saveKey(String uid, Person person, String personName, String cabinet) {
        Key takeKey = new Key(uid, personName, person.getImage(),
                cabinet, timeUtils.getDate(), timeUtils.getTime(), "no time");
        keyViewModel.createKey(takeKey);

        updateTable();
    }

    private void updateTable() {
        keys.clear();
        keys = keyViewModel.getKeyByDate(currentDay);
        model = new KeyTableModel(keys);
        keysTable.setModel(model);
    }
}
