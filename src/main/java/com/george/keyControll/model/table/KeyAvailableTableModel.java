package com.george.keyControll.model.table;

import com.george.keyControll.model.Key;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyAvailableTableModel implements TableModel {

    private final Set<TableModelListener> listeners = new HashSet<>();

    private final List<Key> keys;

    public KeyAvailableTableModel(List<Key> keys) {
        this.keys = keys;
    }
    @Override
    public int getRowCount() {
        return keys.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Кабинет";
            case 1 -> "Доступность";
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
            case 0 -> key.getNumber();
            case 1 -> key.getAvailable();
            default -> "";
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }
}
