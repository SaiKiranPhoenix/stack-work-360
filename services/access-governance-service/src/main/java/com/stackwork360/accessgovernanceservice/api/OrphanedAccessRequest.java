package com.stackwork360.accessgovernanceservice.api;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrphanedAccessRequest(@NotNull List<String> activeWorkerIds) {
}
