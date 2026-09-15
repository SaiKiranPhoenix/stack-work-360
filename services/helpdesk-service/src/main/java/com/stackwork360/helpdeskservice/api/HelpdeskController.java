package com.stackwork360.helpdeskservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.helpdeskservice.application.AddCommentCommand;
import com.stackwork360.helpdeskservice.application.CreateTicketCommand;
import com.stackwork360.helpdeskservice.application.HelpdeskApplicationService;
import com.stackwork360.helpdeskservice.application.UpdateTicketCommand;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/helpdesk/v1/tickets")
public class HelpdeskController {
    private final HelpdeskApplicationService helpdeskApplicationService;

    public HelpdeskController(HelpdeskApplicationService helpdeskApplicationService) {
        this.helpdeskApplicationService = helpdeskApplicationService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateTicketRequest request
    ) {
        TicketResponse response = TicketResponse.from(helpdeskApplicationService.create(new CreateTicketCommand(
                tenantId,
                request.requesterId(),
                request.subject(),
                request.description(),
                request.category(),
                request.priority()
        )));
        return ResponseEntity.created(URI.create("/api/helpdesk/v1/tickets/" + response.id()))
                .body(response);
    }

    @GetMapping
    public List<TicketResponse> list(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) String requesterId,
            @RequestParam(required = false) String assigneeGroup
    ) {
        if (requesterId != null && !requesterId.isBlank()) {
            return helpdeskApplicationService.requesterTickets(tenantId, requesterId).stream().map(TicketResponse::from).toList();
        }
        if (assigneeGroup != null && !assigneeGroup.isBlank()) {
            return helpdeskApplicationService.queue(tenantId, assigneeGroup).stream().map(TicketResponse::from).toList();
        }
        return helpdeskApplicationService.list(tenantId).stream().map(TicketResponse::from).toList();
    }

    @GetMapping("/overdue")
    public List<TicketResponse> overdue(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return helpdeskApplicationService.overdue(tenantId).stream().map(TicketResponse::from).toList();
    }

    @GetMapping("/{ticketId}")
    public TicketResponse get(@PathVariable UUID ticketId) {
        return TicketResponse.from(helpdeskApplicationService.get(ticketId));
    }

    @PutMapping("/{ticketId}")
    public TicketResponse update(
            @PathVariable UUID ticketId,
            @Valid @RequestBody UpdateTicketRequest request
    ) {
        return TicketResponse.from(helpdeskApplicationService.update(ticketId, new UpdateTicketCommand(
                request.subject(),
                request.description(),
                request.category(),
                request.priority()
        )));
    }

    @PatchMapping("/{ticketId}/assign")
    public TicketResponse assign(
            @PathVariable UUID ticketId,
            @Valid @RequestBody AssignTicketRequest request
    ) {
        return TicketResponse.from(helpdeskApplicationService.assign(ticketId, request.assigneeId()));
    }

    @PostMapping("/{ticketId}/comments")
    public TicketResponse addComment(
            @PathVariable UUID ticketId,
            @Valid @RequestBody AddCommentRequest request
    ) {
        return TicketResponse.from(helpdeskApplicationService.addComment(ticketId, new AddCommentCommand(
                request.authorId(),
                request.body(),
                request.internal(),
                request.attachments()
        )));
    }

    @PatchMapping("/{ticketId}/escalate")
    public TicketResponse escalate(
            @PathVariable UUID ticketId,
            @Valid @RequestBody EscalateTicketRequest request
    ) {
        return TicketResponse.from(helpdeskApplicationService.escalate(ticketId, request.actorId(), request.reason()));
    }

    @PatchMapping("/{ticketId}/resolve")
    public TicketResponse resolve(
            @PathVariable UUID ticketId,
            @Valid @RequestBody ResolveTicketRequest request
    ) {
        return TicketResponse.from(helpdeskApplicationService.resolve(ticketId, request.actorId(), request.resolution()));
    }

    @PatchMapping("/{ticketId}/close")
    public TicketResponse close(@PathVariable UUID ticketId) {
        return TicketResponse.from(helpdeskApplicationService.close(ticketId));
    }
}
