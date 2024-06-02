package com.openai.slack.event.dto;

import java.util.Optional;

public class ReplaceOrder extends OrderActionBase {
    public ReplaceOrder(Optional<String> rationale, Optional<String> imageDescription, String action, Optional<String> message) {
        super(rationale, imageDescription, action, message);
    }
}
