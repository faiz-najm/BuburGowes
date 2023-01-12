package com.buburgowes.controller;

import com.buburgowes.view.auth.Login;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.buburgowes.model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author faiz
 */
public abstract class Controller {
    protected static final MysqlDataSource data = new MysqlDataSource();
    protected final ArrayList<User> userList = new ArrayList<>();
    protected final ArrayList<Product> productList = new ArrayList<>();
    protected final ArrayList<Order> orderList = new ArrayList<>();
    public static User currentUser;

    public Controller() {
        this.dbConnection(data);

        // Load product data if not loaded
        if (productList.isEmpty()) {
            this.loadProductData();
        }
    }

    // Abstrac method load order data
    public abstract void loadOrderData();

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


    protected void addProduct(Product product) {
        productList.add(product);
    }

    protected void loadProductData() {
        productList.clear();

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

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

    protected ArrayList<Order> getOrderList() {
        return orderList;
    }

    protected void addOrders(Order order) {
        orderList.add(order);
    }

    // Go to login
    public void goToLogin(JFrame jFrame) {
        Login login = new Login();

        jFrame.dispose();
        login.setVisible(true);
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


    public void addUser(User user) {
        userList.add(user);
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void logoutAccount(
            JFrame jFrame,
            Component parentComponent,
            String current_user
    ) {
        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT * FROM m_user WHERE user_username=?";

            PreparedStatement ps = conn.prepareStatement(getQuery);
            ps.setString(1, current_user);

            // Database connections
            ps.executeQuery();
            String updateQuery = "UPDATE m_user SET status = 0 WHERE user_username=?";

            PreparedStatement pstate = conn.prepareStatement(updateQuery);
            pstate.setString(1, current_user);
            pstate.executeUpdate();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    current_user + " berhasil logout!",
                    "Admin Page",
                    1
            );
            this.goToLogin(jFrame);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal melakukan Sign-out akun! Periksa lagi koneksi Anda!",
                    "Foodiez",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


}
