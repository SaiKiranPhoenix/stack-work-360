package com.stackwork360.goalsokrservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.goalsokrservice.application.GoalsOkrApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/goals-okr/v1")
public class GoalsOkrController {
    private final GoalsOkrApplicationService goalsOkrApplicationService;

    public GoalsOkrController(GoalsOkrApplicationService goalsOkrApplicationService) {
        this.goalsOkrApplicationService = goalsOkrApplicationService;
    }

    @PostMapping("/goals")
    public ResponseEntity<GoalResponse> createGoal(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateGoalRequest request
    ) {
        GoalResponse response = GoalResponse.from(goalsOkrApplicationService.createGoal(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/goals-okr/v1/goals/" + response.id())).body(response);
    }

    @GetMapping("/goals")
    public List<GoalResponse> goals(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return goalsOkrApplicationService.goals(tenantId).stream().map(GoalResponse::from).toList();
    }

    @PatchMapping("/goals/{goalId}/activate")
    public GoalResponse activate(@PathVariable UUID goalId) {
        return GoalResponse.from(goalsOkrApplicationService.activate(goalId));
    }

    @PostMapping("/goals/{goalId}/check-ins")
    public ResponseEntity<GoalCheckInResponse> checkIn(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable UUID goalId,
            @Valid @RequestBody CheckInGoalRequest request
    ) {
        GoalCheckInResponse response = GoalCheckInResponse.from(goalsOkrApplicationService.checkIn(request.toCommand(tenantId, goalId)));
        return ResponseEntity.created(URI.create("/api/goals-okr/v1/goals/" + goalId + "/check-ins/" + response.id())).body(response);
    }

    @GetMapping("/goals/{goalId}/check-ins")
    public List<GoalCheckInResponse> checkIns(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable UUID goalId
    ) {
        return goalsOkrApplicationService.checkIns(tenantId, goalId).stream().map(GoalCheckInResponse::from).toList();
    }

    @GetMapping("/teams/{teamId}/rollup")
    public TeamGoalRollupResponse teamRollup(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String teamId
    ) {
        return TeamGoalRollupResponse.from(goalsOkrApplicationService.teamRollup(tenantId, teamId));
    }

    @GetMapping("/alignment")
    public GoalAlignmentViewResponse alignment(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return GoalAlignmentViewResponse.from(goalsOkrApplicationService.alignment(tenantId));
    }
}
