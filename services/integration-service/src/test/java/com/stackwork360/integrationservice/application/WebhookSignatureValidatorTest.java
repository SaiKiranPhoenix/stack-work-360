package com.stackwork360.integrationservice.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WebhookSignatureValidatorTest {
    private final WebhookSignatureValidator validator = new WebhookSignatureValidator();

    @Test
    void validatesMatchingSignature() {
        String signature = validator.signature("{\"id\":1}", "secret");

        assertTrue(validator.validate("{\"id\":1}", "secret", signature).valid());
    }

    @Test
    void rejectsMismatchedSignature() {
        String signature = validator.signature("{\"id\":1}", "secret");

        assertFalse(validator.validate("{\"id\":2}", "secret", signature).valid());
    }
}
