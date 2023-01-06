package com.buburgowes.controller.auth;

import com.buburgowes.controller.Controller;
import com.buburgowes.model.Admin;
import com.buburgowes.model.Customer;
import com.buburgowes.view.customer.CustomerMain;
import com.buburgowes.view.admin.AdminMain;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class AuthController extends Controller {
    public AuthController() {
        super();
    }

    public void userRegistration(
            JFrame jFrame,
            Component parentComponent,
            String textFullname,
            String textUsername,
            String textPass,
            String textCPass,
            String textAddress,
            String textPhone,
            String comboType
    ) {

        try {
            ArrayList<String> usernameList = new ArrayList<>();
            Connection conn = data.getConnection();
            String getQuery = "SELECT * FROM m_user";


            // Database connections
            Statement state = conn.createStatement();
            ResultSet rset = state.executeQuery(getQuery);

            while (rset.next()) {
                usernameList.add(rset.getString("user_username"));
            }

            // Check if user_username is already exist or not
            if (usernameList.contains(textUsername)) {
                String message = "Username sudah terdaftar!";
                validation(parentComponent, message);

            } else {
                // Check if all fields are filled
                if (textFullname.equals("") || textUsername.equals("") || textPass.equals("") || textCPass.equals("") || textPhone.equals("")) {
                    String message = "Semua bagian harus diisi";
                    validation(parentComponent, message);

                    // Username hanya boleh mengandung huruf, angka, atau tanda strip (-)
                } else if (!textUsername.matches("^[a-zA-Z0-9]+([\\-]?[a-zA-Z0-9]+)*$")) {
                    String message = "Username hanya boleh mengandung huruf, angka, atau tanda strip (-)";
                    validation(parentComponent, message);

                    // Password minimal 8 karakter
                } else if (textPass.length() < 8) {
                    String message = "Password harus memiliki minimal 8 karakter";
                    validation(parentComponent, message);

                    // Password tidak sama
                } else if (!textPass.equals(textCPass)) {
                    String message = "Password tidak sama";
                    validation(parentComponent, message);

                    //validasi nomor telepon hanya boleh angka dan panjangnya 12 karakter (081234567890) dan (6281234567890) dan (02123456789) dan (0221234567) dan (023123456)
                } else if (!textPhone.matches("^[0-9]{12}$") && !textPhone.matches("^[0-9]{13}$") && !textPhone.matches("^[0-9]{11}$") && !textPhone.matches("^[0-9]{10}$") && !textPhone.matches("^[0-9]{9}$")) {
                    String message = "Nomor telepon tidak valid";
                    validation(parentComponent, message);

                    // Validate all form data
                } else {
                    Random random = new Random();

                    int user_type = 0;

                    // validasi user_type (admin atau customer)
                    if (comboType.equals("Admin")) user_type = 1;
                    else if (comboType.equals("Customer")) user_type = 2;

                    //Membuat objek User tipe Admin
                    if (user_type == 1) {

                        // auth token generator (random number) with 6 digits length (000000 - 999999)
                        int auth_token = random.nextInt(999999 + 1);

                        Admin currentUser = new Admin(
                                textUsername.toLowerCase(),
                                textFullname,
                                textPass,
                                textAddress,
                                textPhone,
                                auth_token,
                                1,
                                0
                        );

                        addUser(currentUser);

                        // POST Admin type User data to database
                        postUserAdmin(
                                jFrame,
                                parentComponent,
                                conn,
                                currentUser,
                                textPass);

                        //Membuat objek User tipe Customer
                    } else if (user_type == 2) {
                        Customer currentUser = new Customer(
                                textUsername.toLowerCase(),
                                textFullname,
                                textPass,
                                textAddress,
                                textPhone,
                                2,
                                0,
                                0,
                                0
                        );
                        addUser(currentUser);

                        // POST Customer type User data to database
                        postUserCustomer(
                                jFrame,
                                parentComponent,
                                conn,
                                currentUser,
                                textPass);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void postUserAdmin(JFrame jFrame, Component parentComponent, Connection conn, Admin admin, String textPass) {
        try {
            String insertQuery = "INSERT INTO m_user(user_username, user_fullname, user_pass, user_address, user_phone, user_type, auth_token, status) VALUES(?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstate = conn.prepareStatement(insertQuery);
            pstate.setString(1, admin.getUser_username().toLowerCase());
            pstate.setString(1, admin.getUser_fullName());
            pstate.setString(2, textPass);
            pstate.setString(3, admin.getUser_address());
            pstate.setString(4, admin.getUser_phoneNumber());
            pstate.setInt(5, admin.getUser_type());
            pstate.setInt(6, admin.getUser_token());
            pstate.setInt(7, 0);

            int rowAffected = pstate.executeUpdate();

            if (rowAffected > 0) {
                System.out.println("Debug message : data Admin berhasil ditambahkan");

                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Berhasil mendaftar",
                        "Register",
                        JOptionPane.INFORMATION_MESSAGE
                );
                this.goToLogin(jFrame);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi kesalahan saat membuat akun Anda :( Coba check kembali internet Anda",
                    "Register",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void postUserCustomer(JFrame jFrame, Component parentComponent, Connection conn, Customer customer, String textPass) {
        try {
            String insertQuery = "INSERT INTO " +
                    "m_user(user_username, user_fullname, user_pass, user_address, user_phone, user_type, auth_token, status, user_saldo) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstate = conn.prepareStatement(insertQuery);
            pstate.setString(1, customer.getUser_username().toLowerCase());
            pstate.setString(2, customer.getUser_fullName());
            pstate.setString(3, textPass);
            pstate.setString(4, customer.getUser_address());
            pstate.setString(5, customer.getUser_phoneNumber());
            pstate.setInt(6, customer.getUser_type());
            pstate.setInt(7, 0);
            pstate.setInt(8, 0);
            pstate.setInt(9, 0);

            int rowAffected = pstate.executeUpdate();

            if (rowAffected > 0) {
                System.out.println("Debug message : data Customer berhasil ditambahkan");

                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Berhasil mendaftar",
                        "Register",
                        JOptionPane.INFORMATION_MESSAGE
                );
                this.goToLogin(jFrame);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal membuat akun! Periksa lagi koneksi Anda!",
                    "Register",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // User login
    public void userLogin(
            JFrame jFrame,
            Component parentComponent,
            String textUsername,
            String textPass
    ) {
        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT * FROM m_user WHERE user_username=?";
            // Variable authentication data
            String username = "", pass = "";
            int userType = 0, userState = 0, authToken = 0;

            // Variable for user data
            String fullname = "", address = "", phone = "";
            int userId = 0, userSaldo = 0;

            PreparedStatement pstate = conn.prepareStatement(getQuery);
            pstate.setString(1, textUsername);

            // Database connections
            ResultSet rset = pstate.executeQuery();

            while (rset.next()) {
                username = rset.getString("user_username");
                pass = rset.getString("user_pass");
                userType = rset.getInt("user_type");
                userState = rset.getInt("status");

                userId = rset.getInt("id");
                fullname = rset.getString("user_fullname");
                address = rset.getString("user_address");
                phone = rset.getString("user_phone");
                authToken = rset.getInt("auth_token");
                userSaldo = rset.getInt("user_saldo");

            }

            System.out.format("%s, %s\n", username, pass);

            if (userState == 2) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Akun Anda sedang suspend!",
                        "Authorization Check",
                        JOptionPane.WARNING_MESSAGE
                );

            } else if (username.equals("") || textPass.equals("")) {
                String message = "Username atau Password salah!";
                validation(parentComponent, message);

            } else if (!textUsername.equals(username) || !textPass.equals(pass)) {
                String message = "Username atau Password salah";
                validation(parentComponent, message);

            } else {
                String queryUpdate = "UPDATE m_user SET status = 1 WHERE user_username=?";

                PreparedStatement ps = conn.prepareStatement(queryUpdate);
                ps.setString(1, username);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Berhasil login",
                        "Login",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (userType == 1) {
                    currentUser = new Admin(
                            userId,
                            username,
                            fullname,
                            pass,
                            address,
                            phone,
                            authToken,
                            userType,
                            userState
                    );
                    AdminMain page = new AdminMain((Admin) currentUser);

                    jFrame.dispose();
                    page.setVisible(true);
                } else if (userType == 2) {
                    currentUser = new Customer(userId,
                            username,
                            fullname,
                            pass,
                            address,
                            phone,
                            authToken,
                            userState,
                            userType,
                            userSaldo

                    );
                    CustomerMain page = new CustomerMain((Customer) currentUser);

                    jFrame.dispose();
                    page.setVisible(true);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi kesalahan saat login :( Coba check kembali internet Anda",
                    "Login",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // User login authorization check


        /*String username = tfLoginUsername.getText();
        String password = String.valueOf(tfLoginPassword.getPassword());

        String query = "SELECT * FROM m_user WHERE username = ? AND password = ?";

        try {
            String user = "";
            Connection conn = data();

            //Cek apakah username ada atau tidak
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            *//* while (rs.next()) {
                user = rs.getString("username");
                usernameList.add(user);

            }*//*

         *//* usernameList.forEach((it) -> {
                if (!it.equals(username) && !it.equals(Password)) {
                    JOptionPane.showMessageDialog(null, "Username atau Password Salah");
                } else {
                    JOptionPane.showMessageDialog(null, "Login Berhasil");
                    jTabbedPane1.setSelectedIndex(0);
                }
            });*//*

            // syarat login berhasil jika username dan password ada di database

            if (username.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(null, "Username atau Password Salah");
            } else if (password.length() < 8) {
                JOptionPane.showMessageDialog(null, "Username atau Password Salah");
            } else if (rs.next()) {
                if (username.equals(rs.getString("username")) && password.equals(rs.getString("password"))) {
                    JOptionPane.showMessageDialog(null, "Login Berhasil");
                    Bubur.isLogin = true;
                    this.setVisible(false);
                    new Bubur().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Username atau Password Salah");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }*/

    }

    // User login authorization check
    public void authorizationCheck(
            JFrame jFrame,
            Component parentComponent,
            String current_user
    ) {
        try {
            Connection conn = data.getConnection();
            String getQuery = "SELECT * FROM m_user WHERE user_username=?";

            PreparedStatement ps = conn.prepareStatement(getQuery);
            ps.setString(1, current_user);

            ResultSet rs = ps.executeQuery();
            int user_state = 0;

            while (rs.next()) {
                user_state = rs.getInt("status");
            }

            if (current_user == null || current_user.equals("") || current_user.equals("null") || user_state == 0) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Akses ditolak karena Anda belum melakukan login sebagai Admin!",
                        "Authorization Check",
                        JOptionPane.WARNING_MESSAGE
                );
                this.goToLogin(jFrame);
            } else if (user_state == 2) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Akses ditolak karena akun Anda sedang di-suspend!",
                        "Authorization Check",
                        JOptionPane.WARNING_MESSAGE
                );
                this.goToLogin(jFrame);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Gagal melakukan account authorization! Periksa lagi koneksi Anda!",
                    "Foodiez",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Validation
    private void validation(Component parentComponent, String message) {
        System.out.println("Debug message: " + message);
        JOptionPane.showMessageDialog(
                parentComponent,
                message,
                "Login",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // Logout account
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