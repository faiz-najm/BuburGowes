package com.buburgowes.model;

/**
 * @author andre
 */
public class Transaction {
    private String orderCode;
    private String username, product_name;

    public Transaction(String order_code, String username, String product_name) {
        this.orderCode = order_code;
        this.product_name = product_name;
        this.username = username;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getUsername() {
        return username;
    }

    public String getProduct_name() {
        return product_name;
    }
}
