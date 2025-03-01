package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
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

    private static final Logger LOG = LoggerFactory.getLogger(ResponseHeaderInjector.class);

    private final transient AuditionLogger logger;

    public ResponseHeaderInjector(final AuditionLogger logger) {
        this.logger = logger;
    }

    @Override
    public boolean preHandle(final @NonNull HttpServletRequest request, final @NonNull HttpServletResponse response,
        final @NonNull Object handler) {
        final Span currentSpan = Span.current();
        if (currentSpan != null) {
            final String traceId = currentSpan.getSpanContext().getTraceId();
            final String spanId = currentSpan.getSpanContext().getSpanId();
            logger.info(LOG, "Trace ID: {}, Span ID: {}", traceId, spanId);
            response.addHeader("trace-id", traceId);
            response.addHeader("span-id", spanId);
        } else {
            logger.warn(LOG, "No active span found!");
        }
        return true;
    }
}
