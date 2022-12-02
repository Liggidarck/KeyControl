package com.george.keyControll.view;

import com.george.keyControll.model.Key;
import com.george.keyControll.model.table.InfoTableModel;
import com.george.keyControll.model.table.KeyAvailableTableModel;
import com.george.keyControll.viewModel.KeyViewModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class KeyAvailableView {
    private JTable keysTable;
    public JPanel keyAvailablePanel;
    private JButton refreshButton;

    private final KeyViewModel keyViewModel = new KeyViewModel();
    private ArrayList<Key> keys;
    private ArrayList<Key> allFormattedKeys;

    private TableModel model;

    public KeyAvailableView() {
        keys = keyViewModel.getAllKeys();

        allFormattedKeys = new ArrayList<>();
        for(Key key: keys) {
            String available = "НЕ ДОСТУПЕН";
            if(key.getAvailable().equals("true")) {
                available = "ДОСТУПЕН";
            }

            allFormattedKeys.add(new Key(key.getUid(), key.getNumber(), available));
        }

        model = new KeyAvailableTableModel(allFormattedKeys);
        keysTable.setModel(model);


        refreshButton.addActionListener(e -> {
            keys.clear();
            allFormattedKeys.clear();

            keys = keyViewModel.getAllKeys();

            for(Key key: keys) {
                String available = "НЕ ДОСТУПЕН";
                if(key.getAvailable().equals("true")) {
                    available = "ДОСТУПЕН";
                }

                allFormattedKeys.add(new Key(key.getUid(), key.getNumber(), available));
            }

            model = new KeyAvailableTableModel(allFormattedKeys);
            keysTable.setModel(model);
        });
    }

}
