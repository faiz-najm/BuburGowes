package com.buburgowes.model;

/**
 *
 * @author faiz
 */
public class Product {
    private int id;
    private String product_name, product_desc;
    private int is_available;
    private int product_price;

    public Product(String product_name) {
        this.product_name = product_name;
    }

    public Product(int id, String product_name, String product_desc, int is_available, int product_price) {
        this.id = id;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.product_price = product_price;
        this.is_available = is_available;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public String getAvailableStatus() {

        return switch (getIs_available()) {
            case 0 -> "Tidak Tersedia";
            case 1 -> "Tersedia";
            case 2 -> "Pre Order";
            default -> "";
        };
    }

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }

    public int getProduct_price() {
        return product_price;
    }
}