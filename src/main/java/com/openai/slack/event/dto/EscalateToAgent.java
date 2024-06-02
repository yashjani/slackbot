package com.openai.slack.event.dto;

import java.util.Optional;

public class EscalateToAgent extends FunctionCallBase {
    public EscalateToAgent(Optional<String> rationale, Optional<String> imageDescription, String action, Optional<String> message) {
        super(rationale, imageDescription, action, message);
    }
}
