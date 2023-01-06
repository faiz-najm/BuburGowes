package com.buburgowes.model;

public class OrderDetail {
    private int jumlahPesanan, totalHarga;

    public OrderDetail(int jumlahPesanan, int totalHarga, Product product) {
        this.jumlahPesanan = jumlahPesanan;
        this.totalHarga = totalHarga;
        this.product = product;
    }

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public Product getProduct() {
        return product;
    }

    private Product product;

}
