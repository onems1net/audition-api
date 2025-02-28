package com.audition.configuration;

import io.opentelemetry.api.trace.Span;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ResponseHeaderInjector implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHeaderInjector.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
        @NonNull Object handler) {
        Span currentSpan = Span.current();
        if (currentSpan != null) {
            String traceId = currentSpan.getSpanContext().getTraceId();
            String spanId = currentSpan.getSpanContext().getSpanId();
            logger.info("Trace ID: {}, Span ID: {}", traceId, spanId);
            response.addHeader("trace-id", traceId);
            response.addHeader("span-id", spanId);
        } else {
            logger.warn("No active span found!");
        }
        return true;
    }
}
