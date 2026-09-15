import { apiRequest } from "../../shared/api/http";

export type TenantPlan = "FREE_TRIAL" | "STARTUP" | "ENTERPRISE";
export type DataResidencyRegion = "US" | "EU" | "INDIA" | "APAC";
export type TenantStatus = "ACTIVE" | "SUSPENDED" | "DEACTIVATING" | "DELETED";

export type Tenant = {
  id: string;
  slug: string;
  displayName: string;
  status: TenantStatus;
  plan: TenantPlan;
  dataResidencyRegion: DataResidencyRegion;
  retentionDays: number;
  enabledFeatures: string[];
  configuration: Record<string, string>;
  createdAt: string;
  updatedAt: string;
};

export type CreateTenantRequest = {
  slug: string;
  displayName: string;
  plan: TenantPlan;
  dataResidencyRegion: DataResidencyRegion;
  retentionDays: number;
  enabledFeatures: string[];
  configuration: Record<string, string>;
};

export function listTenants() {
  return apiRequest<Tenant[]>("/api/tenants/v1/tenants");
}

export function createTenant(request: CreateTenantRequest) {
  return apiRequest<Tenant, CreateTenantRequest>("/api/tenants/v1/tenants", {
    method: "POST",
    body: request,
  });
}
