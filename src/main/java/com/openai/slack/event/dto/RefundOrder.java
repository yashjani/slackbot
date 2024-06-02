package com.openai.slack.event.dto;

import java.util.Optional;

public class RefundOrder extends OrderActionBase {
    public RefundOrder(Optional<String> rationale, Optional<String> imageDescription, String action, Optional<String> message) {
        super(rationale, imageDescription, action, message);
    }
}
