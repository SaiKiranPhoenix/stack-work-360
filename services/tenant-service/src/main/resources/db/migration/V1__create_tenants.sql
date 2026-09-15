create table tenants (
    id uuid primary key,
    slug varchar(63) not null,
    display_name varchar(255) not null,
    status varchar(32) not null,
    plan varchar(32) not null,
    data_residency_region varchar(32) not null,
    retention_days integer not null,
    enabled_features text not null default '',
    configuration_json text not null default '{}',
    created_at timestamptz not null,
    updated_at timestamptz not null,
    constraint uq_tenants_slug unique (slug),
    constraint ck_tenants_retention_days check (retention_days between 30 and 3650),
    constraint ck_tenants_slug_format check (slug ~ '^[a-z0-9][a-z0-9-]{2,62}$')
);

create index idx_tenants_status on tenants (status);
create index idx_tenants_plan on tenants (plan);
create index idx_tenants_region on tenants (data_residency_region);
