package com.buburgowes.model;

import java.util.ArrayList;

/**
 * @author faiz
 */
public class Order {
    private String orderNumber;
    private ArrayList<OrderDetail> orderDetails;

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
        this.orderDetails = new ArrayList<>();
    }

    public Order(String orderNumber, ArrayList<OrderDetail> orderDetails) {
        this.orderNumber = orderNumber;
        this.orderDetails = orderDetails;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public Order addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        return this;
    }
}
