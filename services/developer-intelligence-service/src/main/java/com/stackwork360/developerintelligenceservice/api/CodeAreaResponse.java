package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.domain.CodeArea;

public record CodeAreaResponse(
        String pathPattern,
        String ownerGroup
) {
    static CodeAreaResponse from(CodeArea area) {
        return new CodeAreaResponse(area.pathPattern(), area.ownerGroup());
    }
}
