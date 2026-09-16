package com.stackwork360.learningservice.domain;

import java.util.List;

public interface CertificationRepository {
    Certification save(Certification certification);
    List<Certification> findByTenantId(String tenantId);
}
