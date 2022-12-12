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
        return switch (columnIndex) {
            case 0 -> "Фамалия Имя Отчество";
            case 1 -> "Кабинет";
            case 2 -> "Дата";
            case 3 -> "Время получения";
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
        Info info = infos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> info.getPersonName();
            case 1 -> info.getCabinet();
            case 2 -> info.getDateTake();
            case 3 -> info.getTimeTake();
            case 4 -> info.getTimeReturn();
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
        this.listeners.remove(listener);
    }


}
