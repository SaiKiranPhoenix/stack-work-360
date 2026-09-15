import { FormEvent, useEffect, useMemo, useState } from "react";
import { Building2, RefreshCw } from "lucide-react";
import { createTenant, DataResidencyRegion, listTenants, Tenant, TenantPlan } from "./tenantApi";

type TenantFormState = {
  slug: string;
  displayName: string;
  plan: TenantPlan;
  dataResidencyRegion: DataResidencyRegion;
  retentionDays: number;
};

const initialForm: TenantFormState = {
  slug: "acme-platform",
  displayName: "Acme Platform",
  plan: "ENTERPRISE",
  dataResidencyRegion: "INDIA",
  retentionDays: 365,
};

export function TenantAdminPage() {
  const [tenants, setTenants] = useState<Tenant[]>([]);
  const [form, setForm] = useState<TenantFormState>(initialForm);
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  const activeTenants = useMemo(
    () => tenants.filter((tenant) => tenant.status === "ACTIVE").length,
    [tenants],
  );

  async function refreshTenants() {
    setIsLoading(true);
    setMessage(null);
    try {
      setTenants(await listTenants());
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "Unable to load tenants");
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    void refreshTenants();
  }, []);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsLoading(true);
    setMessage(null);
    try {
      const tenant = await createTenant({
        ...form,
        enabledFeatures: ["people-core", "developer-intelligence", "risk-engine"],
        configuration: {
          timezone: form.dataResidencyRegion === "INDIA" ? "Asia/Kolkata" : "UTC",
        },
      });
      setTenants((current) => [...current, tenant]);
      setMessage(`Created tenant ${tenant.displayName}`);
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "Unable to create tenant");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <section className="tenant-page" aria-label="Tenant administration">
      <div className="section-heading">
        <div>
          <p className="eyebrow">Control plane</p>
          <h2>Tenant administration</h2>
        </div>
        <button className="icon-button" type="button" onClick={refreshTenants} disabled={isLoading}>
          <RefreshCw size={18} aria-hidden="true" />
          Refresh
        </button>
      </div>

      <div className="tenant-layout">
        <form className="tenant-form" onSubmit={handleSubmit}>
          <label>
            Slug
            <input
              value={form.slug}
              onChange={(event) => setForm({ ...form, slug: event.target.value })}
              pattern="^[a-z0-9][a-z0-9-]{2,62}$"
              required
            />
          </label>

          <label>
            Display name
            <input
              value={form.displayName}
              onChange={(event) => setForm({ ...form, displayName: event.target.value })}
              required
            />
          </label>

          <label>
            Plan
            <select
              value={form.plan}
              onChange={(event) => setForm({ ...form, plan: event.target.value as TenantPlan })}
            >
              <option value="FREE_TRIAL">Free trial</option>
              <option value="STARTUP">Startup</option>
              <option value="ENTERPRISE">Enterprise</option>
            </select>
          </label>

          <label>
            Data residency
            <select
              value={form.dataResidencyRegion}
              onChange={(event) =>
                setForm({ ...form, dataResidencyRegion: event.target.value as DataResidencyRegion })
              }
            >
              <option value="US">US</option>
              <option value="EU">EU</option>
              <option value="INDIA">India</option>
              <option value="APAC">APAC</option>
            </select>
          </label>

          <label>
            Retention days
            <input
              type="number"
              min={30}
              max={3650}
              value={form.retentionDays}
              onChange={(event) => setForm({ ...form, retentionDays: Number(event.target.value) })}
              required
            />
          </label>

          <button type="submit" disabled={isLoading}>
            <Building2 size={18} aria-hidden="true" />
            Create tenant
          </button>

          {message && <p className="form-message">{message}</p>}
        </form>

        <div className="tenant-table-panel">
          <div className="metric-row">
            <span>{tenants.length} total</span>
            <span>{activeTenants} active</span>
          </div>

          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Plan</th>
                <th>Region</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {tenants.map((tenant) => (
                <tr key={tenant.id}>
                  <td>
                    <strong>{tenant.displayName}</strong>
                    <span>{tenant.slug}</span>
                  </td>
                  <td>{tenant.plan}</td>
                  <td>{tenant.dataResidencyRegion}</td>
                  <td>{tenant.status}</td>
                </tr>
              ))}
              {tenants.length === 0 && (
                <tr>
                  <td colSpan={4}>No tenants loaded yet.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}
