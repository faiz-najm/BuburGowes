package com.buburgowes.model;

/**
 * @author andre
 */
public class Customer extends User {
    private int saldo;

    public Customer(String user_name) {
        super(user_name);
        this.user_username = user_name;
    }

    public Customer(String user_username, String user_fullName, String user_pass, String user_address, String user_phoneNumber, int user_token, int saldo, int user_type, int status) {
        super(user_username, user_fullName, user_pass, user_address, user_phoneNumber, user_token, saldo, user_type, status);
        this.saldo = saldo;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
