package com.stackwork360.integrationservice.application;

import com.stackwork360.integrationservice.domain.WebhookValidationResult;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class WebhookSignatureValidator {
    public WebhookValidationResult validate(String payload, String secret, String providedSignature) {
        if (providedSignature == null || providedSignature.isBlank()) {
            return WebhookValidationResult.invalid("missing signature");
        }
        String expected = "sha256=" + hmacSha256(payload, secret);
        if (!MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), providedSignature.getBytes(StandardCharsets.UTF_8))) {
            return WebhookValidationResult.invalid("signature mismatch");
        }
        return WebhookValidationResult.valid();
    }

    public String signature(String payload, String secret) {
        return "sha256=" + hmacSha256(payload, secret);
    }

    private static String hmacSha256(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("failed to calculate webhook signature", exception);
        }
    }
}
