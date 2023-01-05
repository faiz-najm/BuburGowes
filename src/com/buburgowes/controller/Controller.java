package com.buburgowes.controller;

import com.buburgowes.view.auth.Login;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.buburgowes.model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author andre
 */
public abstract class Controller {
    protected static final MysqlDataSource data = new MysqlDataSource();
    protected final ArrayList<User> userList = new ArrayList<>();
    protected final ArrayList<Product> productList = new ArrayList<>();
    protected final ArrayList<Transaction> transactionList = new ArrayList<>();

    public Controller() {
        this.dbConnection(data);
    }

    public void dbConnection(MysqlDataSource data) {

        String DB_URL = "jdbc:mysql://localhost:3306/db_pbo_buburgowes";
        String DB_Username = "root";

        data.setUrl(DB_URL);
        data.setUser(DB_Username);
        data.setPassword("");

    }

    // Method for check database connection status (true/false)
    public boolean checkDBConnection(Component parentComponent) {
        try {
            Connection conn = data.getConnection();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(parentComponent, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


    // Update user account state
    public void updateUserState(
            Component parentComponent,
            String username,
            int login_state
    ) {
        try {
            Connection conn = data.getConnection();
            String getQuery = "UPDATE m_user SET status=? WHERE user_username=?";

            PreparedStatement ps = conn.prepareStatement(getQuery);
            ps.setInt(1, login_state);
            ps.setString(2, username);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Status akun berhasil diubah!",
                    "Admin Page",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal melakukan perubahan! Periksa lagi koneksi Anda!",
                    "Admin Page",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

  /*  public void insertProductData(
            Component parentComponent,
            String textName,
            String textDesc,
            String comboStatus,
            String textQuantity,
            String textPrice,
            DefaultTableModel tableModel
    ) {
        try {
            String name = "", desc = "";
            Connection conn = data.getConnection();

            if (!productList.isEmpty()) {
                String checkQuery = "SELECT * FROM m_product";

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(checkQuery);

                if (rs.next()) {
                    name = rs.getString("product_name");
                    desc = rs.getString("product_desc");
                }

                if (textName.equals(name) && textDesc.equals(desc)) {
                    JOptionPane.showMessageDialog(
                            parentComponent,
                            "Data produk sudah ada, silakan di-update!",
                            "Add Product",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    String insertQuery = "INSERT INTO m_product(product_name, product_desc, product_qty, product_price, is_available) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    int status = 0;
                    switch (comboStatus) {
                        case "Ada":
                            status = 1;
                            break;
                        case "Pre Order":
                            status = 2;
                            break;
                        case "Habis":
                            status = 0;
                            break;
                    }

                    PreparedStatement ps = conn.prepareStatement(insertQuery);
                    ps.setString(1, textName);
                    ps.setString(2, textDesc);
                    ps.setInt(3, Integer.parseInt(textQuantity));
                    ps.setInt(4, Integer.parseInt(textPrice));
                    ps.setInt(5, status);
                    ps.executeUpdate();

                    addProduct(new Product(
                            textName,
                            textDesc,
                            Integer.parseInt(textQuantity),
                            status,
                            Integer.parseInt(textPrice)
                    ));
                    int index = productList.size() - 1;

                    tableModel.addRow(new Object[]{
                            productList.get(index).getProduct_name(),
                            productList.get(index).getProduct_price(),
                            productList.get(index).getProduct_desc(),
                            productList.get(index).getProduct_qty(),
                            productList.get(index).getAvailableStatus()
                    });

                    JOptionPane.showMessageDialog(
                            parentComponent,
                            "Produk berhasil ditambahkan!",
                            "Admin Page",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } else {
                String insertQuery = "INSERT INTO m_product(product_name, product_desc, product_qty, product_price, is_available) " +
                        "VALUES (?, ?, ?, ?, ?)";

                int status = 0;
                switch (comboStatus) {
                    case "Ada":
                        status = 1;
                        break;
                    case "Pre Order":
                        status = 2;
                        break;
                    case "Habis":
                        status = 0;
                        break;
                }

                PreparedStatement ps = conn.prepareStatement(insertQuery);
                ps.setString(1, textName);
                ps.setString(2, textDesc);
                ps.setInt(3, Integer.parseInt(textQuantity));
                ps.setInt(4, Integer.parseInt(textPrice));
                ps.setInt(5, status);
                ps.executeUpdate();

                addProduct(new Product(
                        textName,
                        textDesc,
                        Integer.parseInt(textQuantity),
                        status,
                        Integer.parseInt(textPrice)
                ));
                int index = productList.size() - 1;

                tableModel.addRow(new Object[]{
                        productList.get(index).getProduct_name(),
                        productList.get(index).getProduct_price(),
                        productList.get(index).getProduct_desc(),
                        productList.get(index).getProduct_qty(),
                        productList.get(index).getAvailableStatus()
                });

                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Produk berhasil ditambahkan!",
                        "Admin Page",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal menambahkan produk baru! Periksa lagi koneksi Anda!",
                    "Admin Page",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }*/

    // Update product data into database
    /*public void updateProductData(
            Component parentComponent,
            String getEditName,
            String getEditDesc,
            int getIs_available,
            int getEditQuantity,
            int getEditPrice,
            DefaultTableModel tableModel,
            String oldProductName
    ) {
        try {
            Connection conn = data.getConnection();
            String setQuery = "UPDATE m_product "
                    + "SET product_name=?, product_desc=?, product_qty=?, product_price=?, is_available=? "
                    + "WHERE product_name=?";

            PreparedStatement psUpdate = conn.prepareStatement(setQuery);

            psUpdate.setString(1, getEditName);
            psUpdate.setString(2, getEditDesc);
            psUpdate.setInt(3, getEditQuantity);
            psUpdate.setInt(4, getEditPrice);
            psUpdate.setInt(5, getIs_available);
            psUpdate.setString(6, oldProductName);
            int affected = psUpdate.executeUpdate();

            if (affected > 0) {
                if (getEditQuantity == 0) {
                    String setStatus = "UPDATE m_product SET is_available = 0 WHERE product_name=?";

                    PreparedStatement pStatus = conn.prepareStatement(setStatus);
                    pStatus.setString(1, getEditName);
                    pStatus.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Pengubahan data produk berhasil!",
                        "Admin Page",
                        1
                );
            }
            productList.clear();

            tableModel.setRowCount(0);
            tableModel.fireTableRowsDeleted(0, productList.size());
            loadProductData(tableModel);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal melakukan perubahan! Periksa lagi koneksi Anda!",
                    "Admin Page",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }*/

    // Delete product data into database
    /*public void deleteProductData(
            Component parentComponent,
            String productName,
            DefaultTableModel tableModel
    ) {
        try {
            Connection conn = data.getConnection();
            String removeForeignKeyCheck = "SET foreign_key_checks = 0";

            Statement st = conn.createStatement();
            st.executeQuery(removeForeignKeyCheck);

            String removeQuery = "DELETE FROM m_product WHERE product_name=?";

            PreparedStatement psDel = conn.prepareStatement(removeQuery);
            psDel.setString(1, productName);
            int affected = psDel.executeUpdate();

            if (affected > 0) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Penghapusan data produk berhasil!",
                        "Admin Page",
                        1
                );
            }
            productList.clear();

            tableModel.setRowCount(0);
            tableModel.fireTableRowsDeleted(0, productList.size());
            loadProductData(tableModel);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal melakukan perubahan! Periksa lagi koneksi Anda!",
                    "Admin Page",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }*/

    public void addUser(User user) {
        userList.add(user);
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    // Go to login
    public void goToLogin(JFrame jFrame) {
        Login login = new Login();

        jFrame.dispose();
        login.setVisible(true);
    }
    public ArrayList<User> getUserList() {
        return userList;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }
}
