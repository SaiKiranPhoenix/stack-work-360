package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.GoalAlignmentNode;
import com.stackwork360.goalsokrservice.domain.GoalAlignmentView;
import java.util.List;

public record GoalAlignmentViewResponse(
        String tenantId,
        List<GoalAlignmentNode> nodes
) {
    static GoalAlignmentViewResponse from(GoalAlignmentView view) {
        return new GoalAlignmentViewResponse(view.tenantId(), view.nodes());
    }
}
