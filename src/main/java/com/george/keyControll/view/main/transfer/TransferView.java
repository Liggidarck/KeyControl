package com.george.keyControll.view.main.transfer;

import com.george.keyControll.Main;
import com.george.keyControll.model.Info;
import com.george.keyControll.model.Person;
import com.george.keyControll.model.table.InfoTableModel;
import com.george.keyControll.utils.TimeUtils;
import com.george.keyControll.viewModel.InfoViewModel;
import com.george.keyControll.viewModel.PersonViewModel;
import gnu.io.NRSerialPort;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class TransferView {
    private JTable infoTransferTable;
    private JButton transferButton;
    private JLabel personLabel;
    public JPanel transferPanel;
    private JLabel scannerLabel;

    private NRSerialPort serial;
    private String port = "no connection";

    private InfoViewModel infoViewModel = new InfoViewModel();

    TimeUtils timeUtils = new TimeUtils();

    List<Info> infoList;

    private TableModel tableModel;
    PersonViewModel personViewModel = new PersonViewModel();


    public TransferView() {
        scannerLabel.setText("ИНИЦИАЛИЗАЦИЯ... ПОЖАЛУЙСТА, ПОДОЖДИТЕ");
        Thread scannerThread = new Thread(() -> {
            int baudRate = 9600;

            for (String ports : NRSerialPort.getAvailableSerialPorts()) {
                System.out.println("Available port: " + ports);
                port = ports;
            }

            if (port.equals("no connection")) {
                JOptionPane.showMessageDialog(transferPanel,
                        "Сканер не найден. Попробуйте подключить сканер в другой разъем." +
                                "\nФункционал программы будет ограничен.",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);

                scannerLabel.setText("СКАНЕР НЕ ПОДКЛЮЧЕН");
            } else {
                serial = new NRSerialPort(port, baudRate);
                serial.connect();

                boolean scan = true;

                while (scan) {
                    String card = scanUidCard();

                    Person person = personViewModel.getPersonByUid(card);
                    if(person == null) {
                        extracted("Пользователь не найден.");
                        return;
                    }

                    scan = false;
                    personLabel.setText("Пользователь: " + person.getName());

                    infoList = infoViewModel.getTransferList(card, timeUtils.getDate());

                    if(infoList.size() == 0) {
                        extracted("Отсуствуют ключи для передачи");
                        return;
                    }

                    if(infoList.size() == 1) {
                        //todo: start edit
                        serial.disconnect();
                        Main.closeTransferView();
                        Main.startTransferToPersonView(infoList.get(0));
                        return;
                    }

                    scannerLabel.setText("Выберите ключ");
                    tableModel = new InfoTableModel(infoList);
                    infoTransferTable.setModel(tableModel);

                }
            }
        });
        scannerThread.start();

        transferButton.addActionListener(e -> {

        });

    }

    private void extracted(String message) {
        JOptionPane.showMessageDialog(transferPanel,
                message,
                "Внимание!",
                JOptionPane.WARNING_MESSAGE);
    }

    private String scanUidCard() {
        System.out.println("\nОТСКАНИРУЙТЕ КАРТУ");
        scannerLabel.setText("ОТСКАНИРУЙТЕ КАРТУ");
        return getUid();
    }

    private String getUid() {
        DataInputStream ins = new DataInputStream(serial.getInputStream());
        String uid;
        try {
            while (true) {
                if (ins.available() > 0) {
                    uid = ins.readLine();
                    if (uid.length() != 0) {
                        System.out.println("uid" + uid);
                        return uid;
                    }
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
