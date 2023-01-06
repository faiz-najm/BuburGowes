package com.buburgowes.model;

public class TempModel {
    private String order_code;
    private String user_fullname;
    private String user_phone;
    private String user_address;
    private String product_name;
    private int id_m_user;
    private int id_m_product;

    public TempModel(String order_code, String user_fullname, String user_phone, String user_address, String product_name, int id_m_user, int id_m_product) {
        this.order_code = order_code;
        this.user_fullname = user_fullname;
        this.user_phone = user_phone;
        this.user_address = user_address;
        this.product_name = product_name;
        this.id_m_user = id_m_user;
        this.id_m_product = id_m_product;
    }

    public String getOrder_code() {
        return order_code;
    }

    public int getId_m_user() {
        return id_m_user;
    }

    public int getId_m_product() {
        return id_m_product;
    }
}