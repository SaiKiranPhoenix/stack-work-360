package com.stackwork360.helpdeskservice.api;

import com.stackwork360.helpdeskservice.domain.AttachmentMetadata;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record AddCommentRequest(
        @NotBlank
        String authorId,

        @NotBlank
        String body,

        boolean internal,

        List<AttachmentMetadata> attachments
) {
}
