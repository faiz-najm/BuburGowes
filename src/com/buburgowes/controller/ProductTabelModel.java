package com.buburgowes.controller;

import javax.swing.table.DefaultTableModel;

public class ProductTabelModel extends DefaultTableModel {

    public ProductTabelModel() {
        super(new String[]{
                "No",
                "Nama",
                "Harga",
                "isAvailable"}, 0);
    }

    public void clearDataTable() {
        this.setRowCount(0);
        this.fireTableRowsDeleted(0, this.getRowCount());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
