package com.stackwork360.learningservice.application;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateCertificationCommand(String tenantId, String name, String issuer, List<UUID> requiredResourceIds, int validityMonths, LocalDate availableFrom) {
}
