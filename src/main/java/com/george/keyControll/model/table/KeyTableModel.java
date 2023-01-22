package com.george.keyControll.model.table;

import com.george.keyControll.model.Info;
import com.george.keyControll.model.Key;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyTableModel implements TableModel {

    private final Set<TableModelListener> listeners = new HashSet<>();

    private final List<Key> keyList;

    public KeyTableModel(List<Key> keyList) {
        this.keyList = keyList;
    }

    @Override
    public int getRowCount() {
        return keyList.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String column = null;

        if (columnIndex == 0) {
            column = "Кабинет";
        }

        return column;
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
        Key key = keyList.get(rowIndex);
        String column = null;

        if (columnIndex == 0) {
            column = key.getNumber();
        }

        return column;
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
        this.listeners.remove(listener);
    }


}
