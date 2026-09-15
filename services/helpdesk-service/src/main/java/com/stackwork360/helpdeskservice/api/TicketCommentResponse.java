package com.stackwork360.helpdeskservice.api;

import com.stackwork360.helpdeskservice.domain.AttachmentMetadata;
import com.stackwork360.helpdeskservice.domain.TicketComment;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TicketCommentResponse(
        UUID id,
        String authorId,
        String body,
        boolean internal,
        List<AttachmentMetadata> attachments,
        Instant createdAt
) {
    public static TicketCommentResponse from(TicketComment comment) {
        return new TicketCommentResponse(
                comment.id(),
                comment.authorId(),
                comment.body(),
                comment.internal(),
                comment.attachments(),
                comment.createdAt()
        );
    }
}
