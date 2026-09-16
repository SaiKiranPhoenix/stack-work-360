package com.stackwork360.benefitsservice.infrastructure;

import com.stackwork360.benefitsservice.domain.OpenEnrollmentWindow;
import com.stackwork360.benefitsservice.domain.OpenEnrollmentWindowRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryOpenEnrollmentWindowRepository implements OpenEnrollmentWindowRepository {
    private final Map<UUID, OpenEnrollmentWindow> windows = new ConcurrentHashMap<>();

    public OpenEnrollmentWindow save(OpenEnrollmentWindow window) {
        windows.put(window.id(), window);
        return window;
    }

    public Optional<OpenEnrollmentWindow> activeWindow(String tenantId, LocalDate date) {
        return windows.values().stream()
                .filter(window -> window.tenantId().equals(tenantId))
                .filter(window -> window.activeOn(date))
                .findFirst();
    }

    public List<OpenEnrollmentWindow> findByTenantId(String tenantId) {
        return windows.values().stream()
                .filter(window -> window.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(OpenEnrollmentWindow::startsOn).reversed())
                .toList();
    }
}
