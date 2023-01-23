package com.george.keyControll.view.main.transfer;

import com.george.keyControll.Main;
import com.george.keyControll.model.Info;
import com.george.keyControll.model.Person;
import com.george.keyControll.utils.TimeUtils;
import com.george.keyControll.viewModel.InfoViewModel;
import com.george.keyControll.viewModel.PersonViewModel;

import javax.swing.*;

public class TransferToPersonView {
    public JPanel transferPanel;
    private JTextField codPersonTextField;
    private JButton searchButton;
    private JTextField namePersonTextField;
    private JTextField uidPeronTextField;
    private JTextField keyTextField;
    private JTextField uidKeyTextField;
    private JButton confirmButton;


    public TransferToPersonView(Info info) {
        int infoId = info.getId();
        namePersonTextField.setText(info.getPersonName());
        uidPeronTextField.setText(info.getPersonUid());
        keyTextField.setText(info.getCabinet());
        uidKeyTextField.setText(info.getCabinetUid());

        PersonViewModel personViewModel = new PersonViewModel();
        InfoViewModel infoViewModel = new InfoViewModel();
        TimeUtils timeUtils = new TimeUtils();

        searchButton.addActionListener(e -> {
            String personId = codPersonTextField.getText();
            Person person = personViewModel.getPersonById(Integer.parseInt(personId));
            if(person == null) {
                JOptionPane.showMessageDialog(transferPanel,
                        "Введен неверный код.",
                        "Внимание!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            namePersonTextField.setText(person.getName());
            uidPeronTextField.setText(person.getUid());
        });

        confirmButton.addActionListener(e -> {
            Info updateInfo = new Info(info.getPersonName(), info.getPersonUid(),
                    info.getCabinet(), info.getCabinetUid(), info.getDateTake(), info.getTimeTake(), timeUtils.getTime());
            infoViewModel.updateInfo(updateInfo, infoId);

            Info createNewInfo = new Info(namePersonTextField.getText(), uidPeronTextField.getText(),
                    info.getCabinet(), info.getCabinetUid(), info.getDateTake(), info.getTimeTake(), "no time");
            infoViewModel.createInfo(createNewInfo);

            Main.closeTransferToPersonView();
            Main.startMainView();
        });


    }

}
