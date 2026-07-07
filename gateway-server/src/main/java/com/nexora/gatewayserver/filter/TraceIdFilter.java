package com.nexora.gatewayserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.nexora.common.util.TraceIdHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceIdFilter — runs first on every request.
 *
 * WHAT IT DOES:
 *   1. Checks if incoming request has X-Trace-Id header (from API Gateway)
 *   2. If yes → use it (so trace continues across services)
 *   3. If no  → generate a new UUID
 *   4. Store in MDC so all logs auto-include it
 *   5. Add X-Trace-Id to response header (so frontend/client can log it too)
 *   6. Always clear MDC after request completes (thread pool safety)
 *
 * THIS IS HOW DISTRIBUTED TRACING WORKS:
 *   Gateway generates traceId → passes to user-service via header
 *   user-service passes same traceId to account-service → Kafka events
 *   Every log line across all services has same traceId
 *   One search in Kibana shows the full journey of one request
 */

@Order(1) // Run before everything else — especially before JwtAuthFilter
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_HEADER = "X-Trace-Id";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // use incoming traceId or create a new one
            String traceId = request.getHeader(TRACE_HEADER);
            if (traceId.isBlank() || traceId == null){
                traceId =   UUID.randomUUID().toString();
            }

            // Store in MDC — all log statements in this thread now include traceId
            TraceIdHolder.set(traceId);
            response.setHeader(TRACE_HEADER, traceId);
            filterChain.doFilter(request, response);
        }
        finally {
            // CRITICAL: always clear MDC after request
            // Thread pool reuses threads — without this, next request inherits old traceId
            TraceIdHolder.clear();
        }
    }
}
