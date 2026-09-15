package com.stackwork360.tenantservice.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackwork360.tenantservice.domain.DataResidencyRegion;
import com.stackwork360.tenantservice.domain.Tenant;
import com.stackwork360.tenantservice.domain.TenantPlan;
import com.stackwork360.tenantservice.domain.TenantRepository;
import com.stackwork360.tenantservice.domain.TenantStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jdbc")
public class JdbcTenantRepository implements TenantRepository {
    private static final TypeReference<Map<String, String>> STRING_MAP = new TypeReference<>() {
    };

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcTenantRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Tenant save(Tenant tenant) {
        int updated = jdbcTemplate.update("""
                update tenants
                   set display_name = ?,
                       status = ?,
                       plan = ?,
                       data_residency_region = ?,
                       retention_days = ?,
                       enabled_features = ?,
                       configuration_json = ?,
                       updated_at = ?
                 where id = ?
                """,
                tenant.displayName(),
                tenant.status().name(),
                tenant.plan().name(),
                tenant.dataResidencyRegion().name(),
                tenant.retentionDays(),
                serializeFeatures(tenant.enabledFeatures()),
                serializeConfiguration(tenant.configuration()),
                tenant.updatedAt(),
                tenant.id()
        );

        if (updated == 0) {
            jdbcTemplate.update("""
                    insert into tenants (
                        id,
                        slug,
                        display_name,
                        status,
                        plan,
                        data_residency_region,
                        retention_days,
                        enabled_features,
                        configuration_json,
                        created_at,
                        updated_at
                    ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    tenant.id(),
                    tenant.slug(),
                    tenant.displayName(),
                    tenant.status().name(),
                    tenant.plan().name(),
                    tenant.dataResidencyRegion().name(),
                    tenant.retentionDays(),
                    serializeFeatures(tenant.enabledFeatures()),
                    serializeConfiguration(tenant.configuration()),
                    tenant.createdAt(),
                    tenant.updatedAt()
            );
        }

        return tenant;
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "select * from tenants where id = ?",
                    this::mapTenant,
                    id
            ));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tenant> findBySlug(String slug) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "select * from tenants where slug = ?",
                    this::mapTenant,
                    slug.toLowerCase(Locale.ROOT)
            ));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tenant> findAll() {
        return jdbcTemplate.query("select * from tenants order by created_at", this::mapTenant);
    }

    @Override
    public boolean existsBySlug(String slug) {
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from tenants where slug = ?",
                Integer.class,
                slug.toLowerCase(Locale.ROOT)
        );
        return count != null && count > 0;
    }

    private Tenant mapTenant(ResultSet resultSet, int rowNum) throws SQLException {
        return Tenant.restore(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("slug"),
                resultSet.getString("display_name"),
                TenantStatus.valueOf(resultSet.getString("status")),
                TenantPlan.valueOf(resultSet.getString("plan")),
                DataResidencyRegion.valueOf(resultSet.getString("data_residency_region")),
                resultSet.getInt("retention_days"),
                deserializeFeatures(resultSet.getString("enabled_features")),
                deserializeConfiguration(resultSet.getString("configuration_json")),
                toInstant(resultSet.getTimestamp("created_at")),
                toInstant(resultSet.getTimestamp("updated_at"))
        );
    }

    private static Instant toInstant(Timestamp timestamp) {
        return timestamp.toInstant();
    }

    private static String serializeFeatures(Set<String> features) {
        return features.stream()
                .map(String::trim)
                .filter(feature -> !feature.isBlank())
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(","));
    }

    private static Set<String> deserializeFeatures(String value) {
        if (value == null || value.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(feature -> !feature.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }

    private String serializeConfiguration(Map<String, String> configuration) {
        try {
            return objectMapper.writeValueAsString(configuration);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("tenant configuration could not be serialized", exception);
        }
    }

    private Map<String, String> deserializeConfiguration(String value) {
        try {
            return objectMapper.readValue(value, STRING_MAP);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("tenant configuration could not be deserialized", exception);
        }
    }
}
