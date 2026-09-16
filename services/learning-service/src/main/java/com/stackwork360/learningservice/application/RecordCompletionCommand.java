package com.stackwork360.learningservice.application;

import java.math.BigDecimal;
import java.util.UUID;

public record RecordCompletionCommand(String tenantId, String workerId, UUID resourceId, BigDecimal score) {
}
