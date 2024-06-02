package com.openai.slack.event.dto;

public class Order {
    private String orderId;
    private String productName;
    private double price;
    private String status;
    private String deliveryDate;

    // Constructor
    public Order(String orderId, String productName, double price, String status, String deliveryDate) {
        this.orderId = orderId;
        this.productName = productName;
        this.price = price;
        this.status = status;
        this.deliveryDate = deliveryDate;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
