package com.buburgowes.model;

/**
 *
 * @author andre
 */
public class Admin extends User {

    public Admin(String user_username, String user_fullName, String user_pass, String user_address, String user_phoneNumber, int user_token, int saldo, int user_type, int status) {
        super(user_username, user_fullName, user_pass, user_address, user_phoneNumber, user_token, saldo, user_type, status);
    }
}