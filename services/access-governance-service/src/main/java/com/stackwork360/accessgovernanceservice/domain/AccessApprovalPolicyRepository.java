package com.stackwork360.accessgovernanceservice.domain;

import java.util.List;

public interface AccessApprovalPolicyRepository {
    AccessApprovalPolicy save(AccessApprovalPolicy policy);
    List<AccessApprovalPolicy> findByTenantId(String tenantId);
}
