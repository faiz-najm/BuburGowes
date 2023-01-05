package com.buburgowes.controller;

import com.buburgowes.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class CustomerController extends Controller {

    public CustomerController() {
        super();
    }


    public static void loadDataOrder() {
        // mengambil data dari database dan menampilkannya di tabel order
        // 1. ambil data dari database dengan query select * from users where username = ? and password = '?
        String username = "admin";
        String password = "admin";
        String query = "SELECT * FROM m_user WHERE user_username = ? AND user_pass = ?";


        try {
            Connection conn = data.getConnection();
            // 2. buat statement
            PreparedStatement ps = conn.prepareStatement(query);

            // 3. set parameter
            ps.setString(1, username);
            ps.setString(2, password);

            // 4. eksekusi query
            ResultSet result = ps.executeQuery();

            // 5. tampilkan data di tabel order (jtable)
            //


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void loadSaldoUser(
            String current_user,
            JLabel labelSaldo
    ) {
        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT user_saldo FROM m_user WHERE user_username=?";

            PreparedStatement ps = conn.prepareStatement(getQuery);
            ps.setString(1, current_user);

            ResultSet rset = ps.executeQuery();

            int saldo = 0;
            while (rset.next()) {
                saldo = rset.getInt("user_saldo");
            }

            labelSaldo.setText("Saldo Anda: Rp" + saldo);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Load transaction data for table
    public void loadTransactionData(DefaultTableModel tableModel) {
        try {
            ArrayList<TempModel> tempList = new ArrayList<>();

            String order_code;
            int id_m_user, id_m_product;

            Connection conn = data.getConnection();
            String getQuery = "SELECT * FROM m_transaction";

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(getQuery);

            while (rset.next()) {
                order_code = rset.getString("order_code");
                id_m_user = rset.getInt("id_m_user");
                id_m_product = rset.getInt("id_m_product");

                tempList.add(new TempModel(order_code, id_m_user, id_m_product));
            }

            tempList.forEach(it -> {
                try {
                    String username, productname;
                    String getData = "SELECT m_user.user_username, m_product.product_name " +
                            "FROM m_user, m_product " +
                            "WHERE m_user.id=? AND m_product.id=?";

                    PreparedStatement ps = conn.prepareStatement(getData);
                    ps.setInt(1, it.getId_m_user());
                    ps.setInt(2, it.getId_m_product());

                    ResultSet dataset = ps.executeQuery();

                    while (dataset.next()) {
                        username = dataset.getString("user_name");
                        productname = dataset.getString("product_name");

                        addTransaction(new Transaction(it.getOrder_code(), username, productname));

                        int index = transactionList.size() - 1;

                        tableModel.addRow(new Object[]{
                                transactionList.get(index).getOrderCode(),
                                transactionList.get(index).getUsername(),
                                transactionList.get(index).getProduct_name()
                        });
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Load user data for table
    public void loadUserData(DefaultTableModel tableModel) {
        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT * FROM m_user";

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(getQuery);

            while (rset.next()) {
                String username = rset.getString("user_name");
                String fullname = rset.getString("user_fullname");
                String pass = rset.getString("user_pass");
                String address = rset.getString("user_address");
                String phone = rset.getString("user_phone");
                int type = rset.getInt("user_type");
                int auth = rset.getInt("auth_token");
                int saldo = rset.getInt("user_saldo");
                int status = rset.getInt("status");

                if (type == 1) addUser(new Admin(
                        username,
                        fullname,
                        pass,
                        address,
                        phone,
                        auth,
                        saldo,
                        status,
                        type
                ));
                else if (type == 2) addUser(new Customer(
                        username,
                        fullname,
                        pass,
                        address,
                        phone,
                        auth,
                        saldo,
                        status,
                        type
                ));
                int index = userList.size() - 1;

                tableModel.addRow(new Object[]{
                        userList.get(index).getUser_username(),
                        userList.get(index).getUser_address(),
                        userList.get(index).getUser_phoneNumber(),
                        userList.get(index).getUserType(),
                        userList.get(index).getUser_token(),
                        userList.get(index).getSaldo(),
                        userList.get(index).getLoginStatus()
                });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Load product data for table
    public void loadProductData(DefaultTableModel tableModel) {
        try {
            Connection conn = data.getConnection();
            String query = "SELECT * FROM m_product";

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(query);

            while (rset.next()) {
                String productName = rset.getString("product_name");
                String productDesc = rset.getString("product_desc");
                int productQty = rset.getInt("product_qty");
                int productPrice = rset.getInt("product_price");
                int productStatus = rset.getInt("is_available");

                addProduct(new Product(
                        productName,
                        productDesc,
                        productQty,
                        productStatus,
                        productPrice
                ));
                int index = productList.size() - 1;

                tableModel.addRow(new Object[]{
                        productList.get(index).getProduct_name(),
                        productList.get(index).getProduct_price(),
                        productList.get(index).getProduct_desc(),
                        productList.get(index).getProduct_qty(),
                        productList.get(index).getAvailableStatus()
                });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Customer transaction
    public void executeTransaction(
            Component parentComponent,
            String current_user,
            String productName,
            int price,
            int buyQuantity,
            int stokBarang,
            int sisaStok,
            int tableRow,
            JLabel labelSaldo,
            DefaultTableModel tableModel
    ) {
        try {
            int saldo = 0;
            Connection conn = data.getConnection();
            String getQuery = "SELECT user_saldo FROM m_user WHERE user_username=?";

            PreparedStatement ps = conn.prepareStatement(getQuery);
            ps.setString(1, current_user);

            ResultSet rset = ps.executeQuery();

            while (rset.next()) {
                saldo = rset.getInt("user_saldo");
            }

            if (sisaStok < 0) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Stok produk yang Anda beli berlebih, produk hanya tersedia " + stokBarang + " buah",
                        "Customer App",
                        JOptionPane.WARNING_MESSAGE
                );
            } else if (saldo < price * buyQuantity) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Saldo Anda tidak mencukupi!",
                        "Customer App",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                Random random = new Random();
                String order_code = "ORDER" + random.nextInt(10000);

                // Insert transaction table
                String insertQuery = "INSERT INTO m_transaction(id_m_user, id_m_product, order_code) " +
                        "VALUES (" +
                        "(SELECT id FROM m_user WHERE user_username=?), " +
                        "(SELECT id FROM m_product WHERE product_name=?)," +
                        "?);";

                String foodName = productList.get(tableRow).getProduct_name();

                PreparedStatement trans = conn.prepareStatement(insertQuery);
                trans.setString(1, current_user);
                trans.setString(2, foodName);
                trans.setString(3, order_code);
                trans.executeUpdate();

                // Update product quantity
                String findFood = "SELECT product_qty FROM m_product WHERE product_name=?";

                PreparedStatement findQty = conn.prepareStatement(findFood);
                findQty.setString(1, productName);
                ResultSet qtySet = findQty.executeQuery();

                int qty = 0;
                while (qtySet.next()) {
                    qty = qtySet.getInt("product_qty");
                }

                int stok = qty - buyQuantity;
                System.out.println(stok);
                String updateQty = "UPDATE m_product SET product_qty=? WHERE product_name=?";

                PreparedStatement pQty = conn.prepareStatement(updateQty);
                pQty.setInt(1, stok);
                pQty.setString(2, productName);
                pQty.executeUpdate();

                // Update user saldo
                int total = price * buyQuantity;
                int sisaSaldo = saldo - total;

                System.out.println(sisaSaldo);
                String updateSaldo = "UPDATE m_user SET user_saldo=? WHERE user_username=?";

                PreparedStatement pSaldo = conn.prepareStatement(updateSaldo);
                pSaldo.setInt(1, sisaSaldo);
                pSaldo.setString(2, current_user);
                pSaldo.executeUpdate();

                addTransaction(new Transaction(order_code, current_user, productName));
                productList.get(tableRow).setProduct_qty(stok);
                tableModel.setRowCount(0);

                userList.forEach(it -> {
                    if (it.getUser_username().equals(current_user)) {
                        it.setSaldo(it.getSaldo() - price * buyQuantity);

                        System.out.println(it.getSaldo());
                    }
                });

                if (stok == 0) {
                    String setStatus = "UPDATE m_product SET is_available = 0 WHERE product_name=?";

                    PreparedStatement pStatus = conn.prepareStatement(setStatus);
                    pStatus.setString(1, productName);
                    pStatus.executeUpdate();

                    productList.get(tableRow).setIs_available(0);
                }

                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Transaksi Berhasil!",
                        "Customer App",
                        1
                );
                labelSaldo.setText("Saldo Anda: Rp...");
                this.loadSaldoUser(current_user, labelSaldo);

                productList.clear();
                loadProductData(tableModel);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal menjalankan transaksi! Periksa lagi koneksi Anda!",
                    "Foodiez Customer",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // Update saldo user
    public void updateSaldoUser(
            Component parentComponent,
            String current_user,
            int setSaldo,
            JLabel labelSaldo
    ) {
        int total = 0;
        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT user_saldo FROM m_user WHERE user_username=?";

            PreparedStatement pSaldo = conn.prepareStatement(getQuery);
            pSaldo.setString(1, current_user);
            ResultSet rset = pSaldo.executeQuery();

            while (rset.next()) {
                total = rset.getInt("user_saldo") + setSaldo;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal melakukan perubahan! Periksa lagi koneksi Anda!",
                    "Customer Page",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        try {
            Connection conn = data.getConnection();
            String updateQuery = "UPDATE m_user SET user_saldo=? WHERE user_username=?";

            PreparedStatement pUpdate = conn.prepareStatement(updateQuery);
            pUpdate.setInt(1, total);
            pUpdate.setString(2, current_user);
            pUpdate.executeUpdate();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Isi saldo berhasil!",
                    "Customer Page",
                    JOptionPane.INFORMATION_MESSAGE
            );

            labelSaldo.setText("");
            this.loadSaldoUser(current_user, labelSaldo);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal melakukan perubahan! Periksa lagi koneksi Anda!",
                    "Customer Page",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
