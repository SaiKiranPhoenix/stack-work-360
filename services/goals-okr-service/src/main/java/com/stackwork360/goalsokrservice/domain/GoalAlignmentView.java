package com.stackwork360.goalsokrservice.domain;

import java.util.List;

public record GoalAlignmentView(
        String tenantId,
        List<GoalAlignmentNode> nodes
) {
}
