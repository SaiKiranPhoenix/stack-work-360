package com.stackwork360.web;

import com.stackwork360.common.CorrelationIds;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    public static final String MDC_CORRELATION_ID = "correlationId";
    public static final String MDC_TENANT_ID = "tenantId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String correlationId = valueOrGenerated(request.getHeader(CorrelationIds.CORRELATION_ID_HEADER));
        String tenantId = request.getHeader(CorrelationIds.TENANT_ID_HEADER);

        MDC.put(MDC_CORRELATION_ID, correlationId);
        if (tenantId != null && !tenantId.isBlank()) {
            MDC.put(MDC_TENANT_ID, tenantId);
        }

        response.setHeader(CorrelationIds.CORRELATION_ID_HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_CORRELATION_ID);
            MDC.remove(MDC_TENANT_ID);
        }
    }

    private static String valueOrGenerated(String value) {
        return value == null || value.isBlank() ? UUID.randomUUID().toString() : value;
    }
}
