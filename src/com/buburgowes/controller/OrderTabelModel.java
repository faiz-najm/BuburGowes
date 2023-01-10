package com.buburgowes.controller;

import com.mysql.cj.conf.ConnectionUrlParser;

import javax.swing.table.DefaultTableModel;

public class OrderTabelModel extends DefaultTableModel {

    public OrderTabelModel() {
        super(new String[]{
                "No",
                "Nama",
                "No Telp",
                "Alamat",
                "Pesanan",
                "Jumlah",
                "Total"}, 0);
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
