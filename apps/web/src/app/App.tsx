import { Activity, Building2, GitBranch, ShieldCheck, Users } from "lucide-react";
import { TenantAdminPage } from "../features/tenants/TenantAdminPage";
import { DashboardCard } from "../shared/ui/DashboardCard";

const modules = [
  {
    title: "People Core",
    description: "Employee 360, lifecycle changes, org structure, documents, leave, payroll prep.",
    icon: Users,
  },
  {
    title: "Developer Intelligence",
    description: "Ownership maps, onboarding progress, access governance, bus-factor risk.",
    icon: GitBranch,
  },
  {
    title: "Workforce Planning",
    description: "Headcount plans, cost simulation, capacity, skills gaps, internal mobility.",
    icon: Building2,
  },
  {
    title: "Risk Engine",
    description: "People, access, compliance, workload, and engineering ownership signals.",
    icon: Activity,
  },
  {
    title: "Governance",
    description: "Audit evidence, policy acknowledgements, compliance controls, zero-trust access.",
    icon: ShieldCheck,
  },
];

export function App() {
  return (
    <main className="app-shell">
      <section className="page-header">
        <p className="eyebrow">stack-work-360</p>
        <h1>People operations and engineering workforce intelligence</h1>
        <p>
          A modular enterprise platform for HR, IT, engineering, finance, security, and leadership teams.
        </p>
      </section>

      <section className="module-grid" aria-label="Product modules">
        {modules.map((module) => (
          <DashboardCard key={module.title} {...module} />
        ))}
      </section>

      <TenantAdminPage />
    </main>
  );
}
