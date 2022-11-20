package com.george.keyControll.model;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyTableModel implements TableModel {

    private final Set<TableModelListener> listeners = new HashSet<>();

    private final List<Key> keys;

    public KeyTableModel(List<Key> keys) {
        this.keys = keys;
    }

    @Override
    public int getRowCount() {
        return keys.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Фамалия Имя Отчество";
            case 1 -> "Кабинет";
            case 2 -> "Дата взятия";
            case 3 -> "Время взятия";
            case 4 -> "Время возврата";
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Key key = keys.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> key.getPersonName();
            case 1 -> key.getCabinet();
            case 2 -> key.getDateTake();
            case 3 -> key.getTimeTake();
            case 4 -> key.getTimeReturn();
            default -> "";
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }


}
