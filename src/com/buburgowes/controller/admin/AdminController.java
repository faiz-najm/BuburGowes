package com.buburgowes.controller.admin;

import com.buburgowes.controller.Controller;
import com.buburgowes.controller.ProductTabelModel;
import com.buburgowes.model.Admin;
import com.buburgowes.model.Order;
import com.buburgowes.model.OrderDetail;
import com.buburgowes.model.Product;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminController extends Controller {

    public Admin currentUser;
    public ProductTabelModel tabelModel;

    public AdminController() {
        super();
        tabelModel = new ProductTabelModel();
        loadProductTabel();
        setCurrentUser(currentUser);

        // Load order data if not loaded
        /*if (orderList.isEmpty()) {
            this.loadOrderData();
        }*/

    }

    @Override
    public void loadOrderData() {
        // Clear table data
        Product product;
        orderList.clear();

        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT *\n" +
                    "FROM m_orders\n";

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(getQuery);

            String orderNumber;

            while (rset.next()) {
                orderNumber = rset.getString("orderNumber");
                addOrders(new Order(orderNumber));
            }

            for (Order tempOrder : getOrderList()) {
                try {
                    int jumlahPesanan, totalHarga;

                    String getData = "SELECT *\n" +
                            "FROM m_orderdetails\n" +
                            "JOIN m_product" +
                            " mp on mp.id = m_orderdetails.id_m_product\n" +
                            "WHERE id_m_orders = '" + tempOrder.getOrderNumber() + "'";

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

                        product = new Product(productCode,
                                productName,
                                productDesc,
                                productPrice,
                                isAvailable);

                        tempOrder.addOrderDetail(
                                new OrderDetail(jumlahPesanan,
                                        totalHarga, product
                                ));

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showAdminView() {
        /*AdminView adminView = new AdminView();
        adminView.setVisible(true);*/
    }

    public void setCurrentUser(Admin currentUser) {
        this.currentUser = currentUser;
    }

    // Load product data for table
    public void loadProductTabel() {

        tabelModel.clearDataTable();
        loadProductData();
        int index = productList.size() - 1;

        try {
            Connection conn = data.getConnection();
            String query = "SELECT * FROM m_product";

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(query);

            // Clear table data before load new data
            while (rset.next()) {
                this.tabelModel.addRow(new Object[]{
                        productList.get(index).getProduct_name(),
                        productList.get(index).getProduct_price(),
                        productList.get(index).getProduct_desc(),
                        productList.get(index).getAvailableStatus()
                });
                index--;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert product data into database
    public void insertProductData(
            Component parentComponent,
            String textNamaProduk,
            String textDesc,
            String comboStatus,
            String textPrice
    ) {
        try {
            String name = "", desc = "";
            Connection conn = data.getConnection();

            if (!productList.isEmpty()) {
                String checkQuery = "SELECT * FROM m_product";

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(checkQuery);


                while (rs.next()) {
                    name = rs.getString("product_name");

                    if (name.equals(textNamaProduk)) {
                        JOptionPane.showMessageDialog(parentComponent,
                                "Produk sudah ada",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                String query = "INSERT INTO m_product (product_name, product_desc, is_available, product_price) VALUES (?, ?, ?, ?)";

                PreparedStatement ps = conn.prepareStatement(query);

                ps.setString(1, textNamaProduk);
                ps.setString(2, textDesc);
                switch (comboStatus) {
                    case "Tersedia" -> ps.setInt(3, 1);
                    case "Kosong" -> ps.setInt(3, 0);
                }
                ps.setString(4, textPrice);

                ps.executeUpdate();

                loadProductTabel();

                JOptionPane.showMessageDialog(parentComponent,
                        "Produk berhasil ditambahkan",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);


            } else {
                String query = "INSERT INTO m_product (product_name, product_desc, is_available, product_price) VALUES (?, ?, ?, ?)";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, textNamaProduk);
                ps.setString(2, textDesc);
                switch (comboStatus) {
                    case "Tersedia" -> ps.setInt(3, 1);
                    case "Kosong" -> ps.setInt(3, 0);
                }
                ps.setString(4, textPrice);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(parentComponent,
                        "Produk berhasil ditambahkan",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Produk gagal ditambahkan",
                    "Add Product",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Update product data into database
    public void updateProductData(
            Component parentComponent,
            String getEditName,
            String getEditDesc,
            int getIs_available,
            int getEditPrice,
            String oldProductName
    ) {
        try {
            Connection conn = data.getConnection();
            String setQuery = "UPDATE m_product "
                    + "SET product_name=?, product_desc=?, product_price=?, is_available=? "
                    + "WHERE product_name=?";

            PreparedStatement psUpdate = conn.prepareStatement(setQuery);

            psUpdate.setString(1, getEditName);
            psUpdate.setString(2, getEditDesc);
            psUpdate.setInt(3, getEditPrice);
            psUpdate.setInt(4, getIs_available);
            psUpdate.setString(5, oldProductName);

            int result = psUpdate.executeUpdate();

            if (result > 0) {
                /*if (getEditQuantity == 0) {
                    String setStatus = "UPDATE m_product SET is_available = 0 WHERE product_name=?";

                    PreparedStatement pStatus = conn.prepareStatement(setStatus);
                    pStatus.setString(1, getEditName);
                    pStatus.executeUpdate();
                }*/

                productList.clear();
                loadProductTabel();

                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Pengubahan data produk berhasil!",
                        "Admin Page",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Pengubahan data produk gagal!",
                        "Admin Page",
                        JOptionPane.ERROR_MESSAGE
                );

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Pengubahan data produk gagal! ",
                    "Admin Page",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Delete product data into database
    public void deleteProductData(
            Component parentComponent,
            String productName
    ) {
        try {
            Connection conn = data.getConnection();

            /*String removeForeignKeyCheck = "SET foreign_key_checks = 0";

            Statement st = conn.createStatement();
            st.executeQuery(removeForeignKeyCheck);*/

            String removeQuery = "DELETE FROM m_product WHERE product_name=?";

            PreparedStatement psDel = conn.prepareStatement(removeQuery);
            psDel.setString(1, productName);

            if (psDel.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Penghapusan data produk berhasil!",
                        "Admin Page",
                        1
                );
            }
            loadProductTabel();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Penghapusan data produk gagal!",
                    "Admin Page",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
