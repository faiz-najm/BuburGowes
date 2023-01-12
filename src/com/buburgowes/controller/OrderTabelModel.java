package com.buburgowes.controller;

import javax.swing.table.DefaultTableModel;

public class OrderTabelModel extends DefaultTableModel {

    public OrderTabelModel(String[] strings, int i) {
        super(strings, i);
    }

    public void clearDataTable() {
        int rowCount = this.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            this.removeRow(i);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }


}
