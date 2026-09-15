package com.stackwork360.security;

import java.util.Set;

public record ActorContext(
        String tenantId,
        String actorId,
        Set<String> roles,
        Set<String> permissions
) {
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
}
