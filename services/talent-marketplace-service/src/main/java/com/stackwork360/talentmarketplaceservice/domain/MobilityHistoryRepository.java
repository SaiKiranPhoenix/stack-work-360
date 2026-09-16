package com.stackwork360.talentmarketplaceservice.domain;

import java.util.List;

public interface MobilityHistoryRepository {
    MobilityHistory save(MobilityHistory history);
    List<MobilityHistory> findByWorker(String tenantId, String workerId);
}
