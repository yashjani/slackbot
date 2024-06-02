package com.openai.slack.event.dto;

public class OrderUtils {

    // Placeholder function to retrieve order details based on the order ID
    public static Order getOrderDetails(String orderId) {
        return new Order(orderId, "Product X", 100.0, "Delivered", "2024-04-10");
    }

    // Placeholder function to escalate the order to a human agent
    public static String escalateToAgent(Order order, String message) {
        return "Order " + order.getOrderId() + " has been escalated to an agent with message: `" + message + "`";
    }

    // Placeholder function to process a refund for the order
    public static String refundOrder(Order order) {
        return "Order " + order.getOrderId() + " has been refunded successfully.";
    }

    // Placeholder function to replace the order with a new one
    public static String replaceOrder(Order order) {
        return "Order " + order.getOrderId() + " has been replaced with a new order.";
    }
}
