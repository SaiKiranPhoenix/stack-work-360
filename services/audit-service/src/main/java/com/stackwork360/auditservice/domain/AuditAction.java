package com.stackwork360.auditservice.domain;

public enum AuditAction {
    CREATE,
    UPDATE,
    DELETE,
    READ,
    SENSITIVE_READ,
    EXPORT,
    LOGIN,
    APPROVE,
    REJECT
}
