package com.phoneshop.dao;

public class Order {
    private int orderID;
    private String customerName;
    private String productName;
    private int amount;
    private int total;
    private String address;
    private String date;
    private String status;

    // ✅ Constructor đầy đủ
    public Order(int orderID, String customerName, String productName, int amount, int total, String date, String address, String status) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.productName = productName;
        this.amount = amount;
        this.total = total;
        this.date = date;
        this.address = address;
        this.status = status;
    }

    // ✅ Constructor rỗng
    public Order() {
    }

    // ✅ Getter & Setter
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }
}
