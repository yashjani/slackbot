package com.openai.slack.event.dto;

import java.util.Optional;

public abstract class FunctionCallBase {
    private Optional<String> rationale;
    private Optional<String> imageDescription;
    private String action;
    private Optional<String> message;

    public FunctionCallBase(Optional<String> rationale, Optional<String> imageDescription, String action, Optional<String> message) {
        this.rationale = rationale;
        this.imageDescription = imageDescription;
        this.action = action;
        this.message = message;
    }

    public String call(String orderId) {
        Order order = OrderUtils.getOrderDetails(orderId);
        switch (action) {
            case "escalate_to_agent":
                return OrderUtils.escalateToAgent(order, message.orElse(""));
            case "replace_order":
                return OrderUtils.replaceOrder(order);
            case "refund_order":
                return OrderUtils.refundOrder(order);
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }
    }

    // Getters and setters
    public Optional<String> getRationale() {
        return rationale;
    }

    public void setRationale(Optional<String> rationale) {
        this.rationale = rationale;
    }

    public Optional<String> getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(Optional<String> imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Optional<String> getMessage() {
        return message;
    }

    public void setMessage(Optional<String> message) {
        this.message = message;
    }
}
