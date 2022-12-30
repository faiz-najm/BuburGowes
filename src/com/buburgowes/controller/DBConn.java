/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.buburgowes.controller;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lenovo
 */
public class DBConn {

    public Connection dbConnection() throws SQLException {
        MysqlDataSource data = new MysqlDataSource();

        String DB_URL = "jdbc:mysql://localhost:3306/javaswing_db4503";
        String DB_Username = "root";

        data.setUrl(DB_URL);
        data.setUser(DB_Username);
        data.setPassword("");

        return data.getConnection();
    }
}
