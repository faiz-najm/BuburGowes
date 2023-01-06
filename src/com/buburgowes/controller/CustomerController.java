package com.buburgowes.controller;

import com.buburgowes.model.*;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class CustomerController extends Controller {

    public CustomerController() {
        super();
    }

    // Load transaction data for table
    public void loadOrdersData(DefaultTableModel tableModel,
                               Customer currentUser) {
        try {
//            ArrayList<TempModel> tempList = new ArrayList<>();

            Connection conn = data.getConnection();
            String getQuery = "SELECT *\n" +
                    "FROM m_orders\n" +
                    "WHERE id_m_user = " + currentUser.getUser_id();

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(getQuery);

            String orderNumber;

            while (rset.next()) {
                orderNumber = rset.getString("orderNumber");
                currentUser.addOrder(new Order(orderNumber));
            }

            currentUser.getOrders().forEach(order -> {
                try {
                    int jumlahPesanan, totalHarga;

                    String getData = "SELECT *\n" +
                            "FROM m_orderdetails\n" +
                            "JOIN m_product mp on mp.id = m_orderdetails.id_m_product\n" +
                            "WHERE id_m_orders = " + order.getOrderNumber();

                    PreparedStatement ps = conn.prepareStatement(getData);

                    ResultSet orderSet = ps.executeQuery();

                    while (orderSet.next()) {
                        // Get data from ResultSet orderDetails table
                        jumlahPesanan = orderSet.getInt("jumlahPesanan");
                        totalHarga = orderSet.getInt("totalHarga");

                        // Get data from ResultSet product table
                        int productCode = orderSet.getInt("id_m_product");
                        String productName = orderSet.getString("product_name");
                        String productDesc = orderSet.getString("product_desc");
                        int productPrice = orderSet.getInt("product_price");
                        int isAvailable = orderSet.getInt("is_available");

                        order.addOrderDetail(
                                new OrderDetail(jumlahPesanan,
                                        totalHarga,
                                        new Product(productCode,
                                                productName,
                                                productDesc,
                                                productPrice,
                                                isAvailable)));

                        int index = order.getOrderDetails().size() - 1;

                        tableModel.addRow(new Object[]{
                                order.getOrderNumber(),
                                currentUser.getUser_fullName(),
                                currentUser.getUser_phoneNumber(),
                                currentUser.getUser_address(),
                                order.getOrderDetails().get(index).getProduct().getProduct_name(),
                                order.getOrderDetails().get(index).getJumlahPesanan(),
                                order.getOrderDetails().get(index).getTotalHarga(),
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

    // Load saldo User
    /*public void loadSaldoUser(
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
    }*/

    // Load user data for table
    public void loadUserData(DefaultTableModel tableModel) {
        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT * FROM m_user";

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(getQuery);

            while (rset.next()) {
                int id = rset.getInt("id");
                String username = rset.getString("user_username");
                String fullname = rset.getString("user_fullname");
                String pass = rset.getString("user_pass");
                String address = rset.getString("user_address");
                String phone = rset.getString("user_phone");
                int type = rset.getInt("user_type");
                int auth = rset.getInt("auth_token");
                int saldo = rset.getInt("user_saldo");
                int status = rset.getInt("status");
                ArrayList<Order> orders = new ArrayList<>();

                if (type == 1) addUser(new Admin(
                        id,
                        username,
                        fullname,
                        pass,
                        address,
                        phone,
                        auth,
                        status,
                        type
                ));
                else if (type == 2) addUser(new Customer(
                        id,
                        username,
                        fullname,
                        pass,
                        address,
                        phone,
                        type,
                        auth,
                        status,
                        saldo
                ));
                int index = userList.size() - 1;

                tableModel.addRow(new Object[]{
                        userList.get(index).getUser_username(),
                        userList.get(index).getUser_address(),
                        userList.get(index).getUser_phoneNumber(),
                        userList.get(index).getUserType(),
                        userList.get(index).getUser_token(),
                        100000,
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
                int productCode = rset.getInt("id");
                String productName = rset.getString("product_name");
                String productDesc = rset.getString("product_desc");
                int productPrice = rset.getInt("product_price");
                int productStatus = rset.getInt("is_available");

                addProduct(new Product(
                        productCode, productName,
                        productDesc,
                        productStatus,
                        productPrice
                ));
                int index = productList.size() - 1;

                tableModel.addRow(new Object[]{
                        productList.get(index).getProduct_name(),
                        productList.get(index).getProduct_price(),
                        productList.get(index).getProduct_desc(),
                        productList.get(index).getAvailableStatus()
                });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Customer transaction
    /*public void executeTransaction(
            Component parentComponent,
            Customer currentUser,
            String productName,
            int price,
            int buyQuantity,
            int isAvailable,
            int tableRow,
            JLabel labelSaldo,
            DefaultTableModel tableModel
    ) {
        try {
            int saldo = 0;
            Connection conn = data.getConnection();
            String getQuery = "SELECT user_saldo FROM m_user WHERE user_username=?";

            PreparedStatement ps = conn.prepareStatement(getQuery);
            ps.setString(1, currentUser.getUser_username());

            ResultSet rset = ps.executeQuery();

            while (rset.next()) {
                saldo = rset.getInt("user_saldo");
            }

            if (isAvailable == 2) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Bubur Ayam " + productName + "tidak tersedia",
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
                String insertQuery = "INSERT INTO m_orders(id_m_user, id_m_product, order_code) " +
                        "VALUES (" +
                        "(SELECT id FROM m_user WHERE user_username=?), " +
                        "(SELECT id FROM m_product WHERE product_name=?)," +
                        "?);";

                String foodName = productList.get(tableRow).getProduct_name();

                PreparedStatement trans = conn.prepareStatement(insertQuery);
                trans.setString(1, currentUser);
                trans.setString(2, foodName);
                trans.setString(3, order_code);
                trans.executeUpdate();

                // Update user saldo
                int total = price * buyQuantity;
                int sisaSaldo = saldo - total;

                System.out.println(sisaSaldo);
                String updateSaldo = "UPDATE m_user SET user_saldo=? WHERE user_username=?";

                PreparedStatement pSaldo = conn.prepareStatement(updateSaldo);
                pSaldo.setInt(1, sisaSaldo);
                pSaldo.setString(2, currentUser.ge);
                pSaldo.executeUpdate();

                addOrders(new Orders(
                        order_code,
                        new ArrayList<OrderDetail>() {{
                            add(new OrderDetail(
                            ));
                        }}
                ));
                tableModel.setRowCount(0);

                userList.forEach(it -> {
                    if (it.getUser_username().equals(currentUser)) {
                        it.setOrder(it.getOrder() - price * buyQuantity);

                        System.out.println(it.getOrder());
                    }
                });

                int avalaible = 1;
                if (avalaible == 0) {
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
                this.loadSaldoUser(currentUser, labelSaldo);

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
    }*/

    // Update saldo user
    /*public void updateSaldoUser(
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
    }*/

}
