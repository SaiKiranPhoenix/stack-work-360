# 00 Product Overview

Version: 0.1  
Status: Draft for architecture review  
Audience: Product, architecture, engineering leadership, domain teams

> Assumption (confirm first): stack-work-360 is a workflow-first People Operations and Engineering Workforce Intelligence platform for technology companies. It combines HRMS, onboarding/offboarding, payroll preparation, employee helpdesk, skills graph, developer access lifecycle, engineering ownership risk, and workforce planning into one enterprise SaaS platform.

## Problem Statement

Technology companies operate people, access, payroll, compliance, engineering ownership, and workforce planning across disconnected tools. HR teams see employee records but not repository ownership. Engineering leaders see delivery risks but not leave, onboarding, succession, or role-change workflows. IT/security teams receive access changes late. Finance receives payroll changes after manual handoffs.

stack-work-360 provides a unified operating layer where employee lifecycle events drive workflows across HR, IT, engineering, finance, security, and compliance.

> Assumption: The first commercial segment is software companies with 250-10,000 workers, including employees, contractors, interns, and external vendors.

## Target Users

| User group | Primary needs |
|---|---|
| HR operations | Employee records, onboarding, leave, documents, policy workflows, cases |
| People partners | Org planning, performance signals, employee lifecycle visibility |
| Engineering managers | Developer onboarding, capacity, skills, ownership risk, project staffing |
| IT/security | Access lifecycle, asset tracking, offboarding controls, audit evidence |
| Finance/payroll | Payroll preparation, compensation changes, contractor invoices, cost simulation |
| Employees | Self-service profile, leave, tickets, documents, policies, internal opportunities |
| Executives | Headcount, cost, risk, workforce analytics, compliance posture |

## Value Proposition

1. Create one governed source of truth for employee lifecycle state.
2. Automate cross-functional workflows triggered by HR, engineering, and access events.
3. Reduce developer ramp-up time through structured onboarding and access provisioning.
4. Identify workforce and engineering ownership risks before they become incidents.
5. Provide audit-ready evidence for access, compliance, payroll preparation, and HR decisions.

## Product Pillars

| Pillar | Description |
|---|---|
| People Core | Employee profiles, org structure, teams, locations, lifecycle states |
| Lifecycle Automation | Onboarding, role changes, transfers, offboarding, approvals |
| Developer Workforce Intelligence | Repository ownership, engineering skills, access lifecycle, bus-factor risk |
| People Risk Engine | Cross-domain risk detection from HR, leave, access, performance, and engineering events |
| Workforce Planning | Hiring plans, cost simulation, skill gaps, capacity analysis |
| Governance | Audit logs, policy acknowledgements, access reviews, compliance evidence |

## Success Metrics

| Metric | Target direction |
|---|---|
| New-hire time to productivity | Decrease |
| Developer access fulfillment time | Decrease |
| Offboarding access removal SLA compliance | Increase |
| Payroll preparation corrections | Decrease |
| Critical service single-owner risks | Decrease |
| HR ticket first response time | Decrease |
| Internal mobility fill rate | Increase |
| Audit evidence collection time | Decrease |

## Out of Scope for Initial Product

> Assumption: stack-work-360 prepares payroll and exports payroll data but does not move money, file taxes, or become the system of record for statutory payroll in MVP.

Out of scope for MVP:

- Real payroll disbursement.
- Native applicant tracking system.
- Full learning management system.
- Real-time employee surveillance.
- Production identity provider replacement.
- Legal advice or automated employment-law decisions.

See [01-requirements.md](01-requirements.md) for phase boundaries.
