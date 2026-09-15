package com.stackwork360.developerintelligenceservice.infrastructure;

import com.stackwork360.developerintelligenceservice.domain.CodeRepository;
import com.stackwork360.developerintelligenceservice.domain.CodeRepositoryRepository;
import com.stackwork360.developerintelligenceservice.domain.RepositoryProvider;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCodeRepositoryRepository implements CodeRepositoryRepository {
    private final Map<UUID, CodeRepository> repositories = new ConcurrentHashMap<>();

    @Override
    public CodeRepository save(CodeRepository repository) {
        repositories.put(repository.id(), repository);
        return repository;
    }

    @Override
    public Optional<CodeRepository> findById(UUID id) {
        return Optional.ofNullable(repositories.get(id));
    }

    @Override
    public Optional<CodeRepository> findByTenantIdAndProviderAndExternalId(String tenantId, RepositoryProvider provider, String externalId) {
        return repositories.values().stream()
                .filter(repository -> repository.tenantId().equals(tenantId))
                .filter(repository -> repository.provider() == provider)
                .filter(repository -> repository.externalId().equals(externalId))
                .findFirst();
    }

    @Override
    public List<CodeRepository> findByTenantId(String tenantId) {
        return repositories.values().stream()
                .filter(repository -> repository.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(CodeRepository::name))
                .toList();
    }
}
