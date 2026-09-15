const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "";

export async function apiRequest<TResponse, TBody = unknown>(
  path: string,
  options: {
    method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
    body?: TBody;
    tenantId?: string;
  } = {},
): Promise<TResponse> {
  const headers = new Headers({
    Accept: "application/json",
    "X-Correlation-Id": crypto.randomUUID(),
  });

  if (options.body !== undefined) {
    headers.set("Content-Type", "application/json");
  }

  if (options.tenantId) {
    headers.set("X-Tenant-Id", options.tenantId);
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: options.method ?? "GET",
    headers,
    body: options.body === undefined ? undefined : JSON.stringify(options.body),
  });

  if (!response.ok) {
    const problem = await response.json().catch(() => undefined);
    throw new Error(problem?.message ?? `Request failed with ${response.status}`);
  }

  return response.json() as Promise<TResponse>;
}
