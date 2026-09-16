package com.stackwork360.skillsgraphservice.domain;

import java.util.List;

public record SkillGapReport(
        String tenantId,
        String workerId,
        List<SkillGap> gaps
) {
}
