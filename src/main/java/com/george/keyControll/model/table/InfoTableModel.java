package com.george.keyControll.model.table;

import com.george.keyControll.model.Info;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InfoTableModel implements TableModel {

    private final Set<TableModelListener> listeners = new HashSet<>();

    private final List<Info> infos;

    public InfoTableModel(List<Info> infos) {
        this.infos = infos;
    }

    @Override
    public int getRowCount() {
        return infos.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String column = null;

        if (columnIndex == 0) {
            column = "Фамалия Имя Отчество";
        }
        if (columnIndex == 1) {
            column = "Кабинет";
        }
        if (columnIndex == 2) {
            column = "Дата";
        }
        if (columnIndex == 3) {
            column = "Время получения";
        }
        if (columnIndex == 4) {
            column = "Время возврата";
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
        Info info = infos.get(rowIndex);
        String column = null;

        if (columnIndex == 0) {
            column = info.getPersonName();
        }
        if (columnIndex == 1) {
            column = info.getCabinet();
        }
        if (columnIndex == 2) {
            column = info.getDateTake();
        }
        if (columnIndex == 3) {
            column = info.getTimeTake();
        }
        if (columnIndex == 4) {
            column = info.getTimeReturn();
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
