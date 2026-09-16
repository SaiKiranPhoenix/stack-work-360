package com.stackwork360.skillsgraphservice.application;

import java.util.List;

public record CreateCertificationMappingCommand(String tenantId, String certificationCode, List<String> skillCodes) {
}
