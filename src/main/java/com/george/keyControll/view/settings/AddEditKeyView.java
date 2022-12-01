package com.george.keyControll.view.settings;

import com.george.keyControll.Main;
import com.george.keyControll.model.Key;
import com.george.keyControll.utils.TextValidator;
import com.george.keyControll.viewModel.KeyViewModel;

import javax.swing.*;

public class AddEditKeyView {
    private JTextField uidTextField;
    private JTextField numberTextField;
    private JButton scanButton;
    private JCheckBox availableCheckBox;
    private JButton saveButton;
    public JPanel addEditPanel;
    private JButton deleteButton;

    private final TextValidator textValidator = new TextValidator();
    private final KeyViewModel keyViewModel = new KeyViewModel();

    public AddEditKeyView(Key key) {

        saveButton.addActionListener(e -> {
            Key newKey = getKey();

            if (newKey == null) {
                JOptionPane.showMessageDialog(addEditPanel,
                        "Поля не могут быть пустыми.",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            if (key == null) {
                keyViewModel.createKey(newKey);
            } else {
                int id = key.getId();
                newKey.setId(id);
                keyViewModel.updateKey(newKey);
            }

            Main.startSettingsView();
            Main.closeAddEditKey();
        });

        if (key == null) {
            return;
        }


        uidTextField.setText(key.getUid());
        numberTextField.setText(key.getNumber());
        String available = key.getAvailable();
        availableCheckBox.setSelected(available.equals("true"));

        deleteButton.setVisible(true);
        deleteButton.addActionListener(e -> {
            int id = key.getId();
            keyViewModel.deleteKey(id);
            Main.closeAddEditKey();
            Main.startSettingsView();
        });

    }

    private boolean validateFields(String uid, String number, String available) {
        return textValidator.isEmptyField(uid) || textValidator.isEmptyField(number) || textValidator.isEmptyField(available);
    }
    private Key getKey() {
        String uid = uidTextField.getText();
        String number = numberTextField.getText();
        boolean isAvailable = availableCheckBox.isSelected();
        String available = "false";
        if (isAvailable) {
            available = "true";
        }

        if(validateFields(uid, number, available)) {
            return null;
        }

        return new Key(uid, number, available);
    }

}
