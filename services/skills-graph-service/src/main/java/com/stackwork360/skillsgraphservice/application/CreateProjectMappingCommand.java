package com.stackwork360.skillsgraphservice.application;

import java.util.List;

public record CreateProjectMappingCommand(String tenantId, String projectCode, List<String> skillCodes) {
}
