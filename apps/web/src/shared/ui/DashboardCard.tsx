import type { LucideIcon } from "lucide-react";

type DashboardCardProps = {
  title: string;
  description: string;
  icon: LucideIcon;
};

export function DashboardCard({ title, description, icon: Icon }: DashboardCardProps) {
  return (
    <article className="dashboard-card">
      <Icon aria-hidden="true" size={28} strokeWidth={1.8} />
      <h2>{title}</h2>
      <p>{description}</p>
    </article>
  );
}
