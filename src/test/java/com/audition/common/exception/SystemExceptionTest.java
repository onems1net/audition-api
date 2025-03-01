package com.audition.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class SystemExceptionTest {

    private static final String CAUSE = "Cause";
    private static final String SYSTEM_ERROR_DETAIL = "System error detail";

    @Test
    void testDefaultConstructor() {
        final SystemException exception = new SystemException();
        assertNull(exception.getMessage());
        assertNull(exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessage() {
        final String message = "System error";
        final SystemException exception = new SystemException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessageAndErrorCode() {
        final String message = "System error";
        final Integer errorCode = 500;
        final SystemException exception = new SystemException(message, errorCode);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessageAndThrowable() {
        final String message = "System error";
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailTitleAndErrorCode() {
        final String title = "System Error Title";
        final Integer errorCode = 500;
        final SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, title, errorCode);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
    }

    @Test
    void testConstructorWithDetailTitleAndThrowable() {
        final String title = "System Error Title";
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, title, cause);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(500, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailErrorCodeAndThrowable() {
        final Integer errorCode = 500;
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, errorCode, cause);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailTitleErrorCodeAndThrowable() {
        final String title = "System Error Title";
        final Integer errorCode = 500;
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, title, errorCode, cause);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }
}