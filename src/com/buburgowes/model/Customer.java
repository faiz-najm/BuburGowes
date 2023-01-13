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
        this.userUsername = user_name;
    }

    public Customer(int id,
                    String userUsername,
                    String userFullName,
                    String userPass,
                    String userAddress,
                    String userPhoneNumber,
                    int userType,
                    int userToken,
                    int status,
                    int saldo
    ) {
        super(id,
                userUsername,
                userFullName,
                userPass,
                userAddress,
                userPhoneNumber,
                userType,
                userToken,
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
