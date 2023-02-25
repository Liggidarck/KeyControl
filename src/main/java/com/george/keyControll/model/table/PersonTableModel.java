package com.george.keyControll.model.table;

import com.george.keyControll.model.Person;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonTableModel implements TableModel {

    private final Set<TableModelListener> listeners = new HashSet<>();

    private final List<Person> personList;

    public PersonTableModel(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public int getRowCount() {
        return personList.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String column = null;

        if (columnIndex == 0) {
            column = "Пользователь";
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
        Person person = personList.get(rowIndex);
        String column = null;

        if (columnIndex == 0) {
            column = person.getName();
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
