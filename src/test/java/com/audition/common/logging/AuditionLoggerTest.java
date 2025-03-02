package com.audition.common.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;

@SuppressWarnings("PMD.LoggerIsNotStaticFinal")
class AuditionLoggerTest {

    @Mock
    private transient Logger logger;

    @InjectMocks
    private transient AuditionLogger auditionLogger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditionLogger = new AuditionLogger();
    }

    @Test
    void testInfo() {
        final String message = "Info message";
        when(logger.isInfoEnabled()).thenReturn(true);

        auditionLogger.info(logger, message);

        verify(logger).info(message);
    }

    @Test
    void testInfoWithObject() {
        final String message = "Info message with object";
        final Object object = new Object();
        when(logger.isInfoEnabled()).thenReturn(true);

        auditionLogger.info(logger, message, object);

        verify(logger).info(message, object);
    }

    @Test
    void testDebug() {
        final String message = "Debug message";
        when(logger.isDebugEnabled()).thenReturn(true);

        auditionLogger.debug(logger, message);

        verify(logger).debug(message);
    }

    @Test
    void testWarn() {
        final String message = "Warn message";
        when(logger.isWarnEnabled()).thenReturn(true);

        auditionLogger.warn(logger, message);

        verify(logger).warn(message);
    }

    @Test
    void testError() {
        final String message = "Error message";
        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.error(logger, message);

        verify(logger).error(message);
    }

    @Test
    void testLogErrorWithException() {
        final String message = "Error message with exception";
        final Exception exception = new Exception("Exception message");
        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.logErrorWithException(logger, message, exception);

        verify(logger).error(message, exception);
    }

    @Test
    void testLogStandardProblemDetail() {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(500);
        final Exception exception = new Exception("Exception message");
        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.logStandardProblemDetail(logger, problemDetail, exception);

        verify(logger).error(anyString(), eq(exception));
    }

    @Test
    void testLogHttpStatusCodeError() {
        final String message = "HTTP status code error";
        final Integer errorCode = 400;
        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.logHttpStatusCodeError(logger, message, errorCode);

        verify(logger).error(anyString(), eq("Error Code: 400, Message: HTTP status code error"));
    }
}