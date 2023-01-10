package com.buburgowes.model;

import java.util.ArrayList;

/**
 * @author faiz
 */
public class Customer extends User {
    private ArrayList<Order> orders;
    private int saldo;

    public Customer(String user_name) {
        super(user_name);
        this.user_username = user_name;
    }

    public Customer(int id,
                    String user_username,
                    String user_fullName,
                    String user_pass,
                    String user_address,
                    String user_phoneNumber,
                    int user_type,
                    int user_token,
                    int status,
                    int saldo
    ) {
        super(id,
                user_username,
                user_fullName,
                user_pass,
                user_address,
                user_phoneNumber,
                user_type,
                user_token,
                status
        );

        this.saldo = saldo;
        this.orders = new ArrayList<>();
    }


    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
