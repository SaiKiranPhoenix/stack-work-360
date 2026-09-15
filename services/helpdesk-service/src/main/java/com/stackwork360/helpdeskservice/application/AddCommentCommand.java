package com.stackwork360.helpdeskservice.application;

import com.stackwork360.helpdeskservice.domain.AttachmentMetadata;
import java.util.List;

public record AddCommentCommand(
        String authorId,
        String body,
        boolean internal,
        List<AttachmentMetadata> attachments
) {
}
