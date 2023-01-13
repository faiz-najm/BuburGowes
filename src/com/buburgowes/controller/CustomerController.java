package com.buburgowes.controller;

import com.buburgowes.model.*;
import com.buburgowes.view.customer.CustomerMain;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CustomerController extends Controller {

    public Customer currentUser;
    public OrderTabelModel tabelModel;

    public CustomerController() {
        super();
        tabelModel = new OrderTabelModel(new String[]{"No", "Nama", "No Telp", "Alamat", "Pesanan", "Jumlah", "Total"}, 0);
        setCurrentUser(currentUser);

    }

    @Override
    public void loadOrderData() {

    }

    public Customer getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Customer currentUser) {
        this.currentUser = currentUser;
    }

    // Load transaction data for table
    public void loadOrderTabel() {
        // Clear table data
        tabelModel.clearDataTable();
        currentUser.getOrders().clear();

        Product product;

        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT *\n" +
                    "FROM m_orders\n" +
                    "WHERE id_m_user = " + this.currentUser.getUserId();

            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(getQuery);

            String orderNumber, orderAddress;

            while (rset.next()) {
                orderNumber = rset.getString("orderNumber");
                orderAddress = rset.getString("alamat");
                currentUser.addOrder(new Order(orderNumber, orderAddress));
            }

            for (Order order : currentUser.getOrders()) {
                try {
                    int jumlahPesanan, totalHarga;

                    String getData = "SELECT *\n" +
                            "FROM m_orderdetails\n" +
                            "JOIN m_product" +
                            " mp on mp.id = m_orderdetails.id_m_product\n" +
                            "WHERE id_m_orders = '" + order.getOrderNumber() + "'";

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

                        order.addOrderDetail(
                                new OrderDetail(jumlahPesanan,
                                        totalHarga, product
                                ));

                        int orderDetailSize = order.getOrderDetails().size() - 1;

                        // overide Object contructor class
                        Object[] data = {
                                order.getOrderNumber(),
                                currentUser.getUserFullName(),
                                currentUser.getUserPhoneNumber(),
                                order.getAlamat(),
                                order.getOrderDetails().get(orderDetailSize).getProduct().getProduct_name(),
                                order.getOrderDetails().get(orderDetailSize).getJumlahPesanan(),
                                order.getOrderDetails().get(orderDetailSize).getTotalHarga()
                        };


                        tabelModel.addRow(data);

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void executeOrder(CustomerMain parentComponent, String alamat, int jumlahBubur1, int jumlahBubur2, int jumlahBubur3) {
        try {
            Connection conn = data.getConnection();

            String insertOrder = "INSERT INTO m_orders (orderNumber, tanggalPesan, alamat, id_m_user) VALUES (?, ?, ?, ?)";

            //Generate order number getGeneratedKeys from database and set it to orderNumber
            PreparedStatement psOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);

            //Get current date and time
            ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
            Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
            String orderNumber = "ORD" + timestamp.toString().replace(" ", "").replace("-", "").replace(":", "").replace(".", "");

            //Set orderNumber to orderNumber
            psOrder.setString(1, orderNumber);
            psOrder.setTimestamp(2, timestamp);
            psOrder.setString(3, alamat);
            psOrder.setInt(4, currentUser.getUserId());

//                psOrder.setDate(2, Date.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Jakarta")).toLocalDate()));

            psOrder.executeUpdate();

            if (jumlahBubur1 > 0) {
                String insertOrderDetail = "INSERT INTO m_orderdetails (id_m_orders, id_m_product, jumlahPesanan, totalHarga) VALUES (?, ?, ?, ?)";

                int totalHargaBubur = productList.get(0).getProduct_price() * jumlahBubur1;

                PreparedStatement psOrderDetail = conn.prepareStatement(insertOrderDetail);
                psOrderDetail.setString(1, orderNumber);
                psOrderDetail.setInt(2, 1);
                psOrderDetail.setInt(3, jumlahBubur1);
                psOrderDetail.setInt(4, totalHargaBubur);

                try {
                    psOrderDetail.executeUpdate();
                    currentUser.addOrder(new Order(orderNumber, alamat).addOrderDetail(
                            new OrderDetail(jumlahBubur1,
                                    totalHargaBubur,
                                    productList.get(0))));

                    tabelModel.addRow(new Object[]{
                            orderNumber,
                            currentUser.getUserFullName(),
                            currentUser.getUserPhoneNumber(),
                            alamat,
                            productList.get(0).getProduct_name(),
                            jumlahBubur1,
                            totalHargaBubur
                    });

                } catch (SQLException e) {
                    conn.rollback();
                    System.out.println(e.getMessage());
                }
            }

            if (jumlahBubur2 > 0) {
                String insertOrderDetail = "INSERT INTO m_orderdetails (id_m_orders, id_m_product, jumlahPesanan, totalHarga) VALUES (?, ?, ?, ?)";

                int totalHargaBubur = productList.get(1).getProduct_price() * jumlahBubur2;


                PreparedStatement psOrderDetail = conn.prepareStatement(insertOrderDetail);
                psOrderDetail.setString(1, orderNumber);
                psOrderDetail.setInt(2, 2);
                psOrderDetail.setInt(3, jumlahBubur2);
                psOrderDetail.setInt(4, totalHargaBubur);

                try {
                    psOrderDetail.executeUpdate();
                    currentUser.addOrder(new Order(orderNumber, alamat).addOrderDetail(
                            new OrderDetail(jumlahBubur2,
                                    totalHargaBubur,
                                    productList.get(2))));

                    tabelModel.addRow(new Object[]{
                            orderNumber,
                            currentUser.getUserFullName(),
                            currentUser.getUserPhoneNumber(),
                            alamat,
                            productList.get(1).getProduct_name(),
                            jumlahBubur2,
                            totalHargaBubur
                    });
                } catch (SQLException e) {
                    conn.rollback();
                    System.out.println(e.getMessage());
                }
            }

            if (jumlahBubur3 > 0) {
                String insertOrderDetail = "INSERT INTO m_orderdetails (id_m_orders, id_m_product, jumlahPesanan, totalHarga) VALUES (?, ?, ?, ?)";

                int totalHargaBubur = productList.get(2).getProduct_price() * jumlahBubur3;

                PreparedStatement psOrderDetail = conn.prepareStatement(insertOrderDetail);
                psOrderDetail.setString(1, orderNumber);
                psOrderDetail.setInt(2, 3);
                psOrderDetail.setInt(3, jumlahBubur3);
                psOrderDetail.setInt(4, totalHargaBubur);

                // jika gagal menginsert data ke table m_orderdetails maka akan rollback data yang sudah diinsert ke table m_orders
                try {
                    psOrderDetail.executeUpdate();
                    currentUser.addOrder(new Order(orderNumber, alamat).addOrderDetail(
                            new OrderDetail(jumlahBubur1,
                                    totalHargaBubur,
                                    productList.get(2))));
                    tabelModel.addRow(new Object[]{
                            orderNumber,
                            currentUser.getUserFullName(),
                            currentUser.getUserPhoneNumber(),
                            alamat,
                            productList.get(2).getProduct_name(),
                            jumlahBubur3,
                            totalHargaBubur
                    });
                } catch (SQLException e) {
                    conn.rollback();
                    System.out.println(e.getMessage());
                }
            }
            // jika berhasil menginsert data ke table m_orderdetails maka akan commit data yang sudah diinsert ke table m_orders
            JOptionPane.showMessageDialog(parentComponent, "Pesanan Anda berhasil diproses");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void cancelOrderData(
            Component parentComponent,
            String orderNumber) {
        try {
            Connection conn = data.getConnection();
           /* String removeForeignKeyCheck = "SET foreign_key_checks = 0";

            Statement st = conn.createStatement();
            st.executeQuery(removeForeignKeyCheck);*/

            String removeQuery = "DELETE FROM m_orders WHERE orderNumber=?";

            PreparedStatement psDel = conn.prepareStatement(removeQuery);
            psDel.setString(1, orderNumber);

            if (psDel.executeUpdate() > 0) {
                loadOrderTabel();
                JOptionPane.showMessageDialog(parentComponent, "Pesanan Anda dibatalkan");
            } else {
                JOptionPane.showMessageDialog(parentComponent, "Pesanan tidak dapat dibatalkan");
            }

//            productList.clear();

/*            tabelModel.setRowCount(0);
            tabelModel.fireTableRowsDeleted(0, productList.size());*/

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

    // Execute Transaction
    /*public void executeTransaction(
            Component parentComponent,
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
            ps.setString(1, currentUser.getUser_username());

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
                String insertQuery = "INSERT INTO m_orders (orderNumber, id_m_user) VALUES (?, ?)" + order_code + ", " + currentUser.getUser_id();

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
                String updateSaldo = "UPDATE m_user SET user_saldo=? WHERE user_name=?";

                PreparedStatement pSaldo = conn.prepareStatement(updateSaldo);
                pSaldo.setInt(1, sisaSaldo);
                pSaldo.setString(2, current_user);
                pSaldo.executeUpdate();

                addTransaction(new Transaction(order_code, current_user, productName));
                productList.get(tableRow).setProduct_qty(stok);
                tableModel.setRowCount(0);

                userList.forEach(it -> {
                    if (it.getUser_name().equals(current_user)) {
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
    }*/

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

    /*public void loadUserData(DefaultTableModel tableModel) {
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
