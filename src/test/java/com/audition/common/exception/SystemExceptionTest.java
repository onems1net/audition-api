package com.audition.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class SystemExceptionTest {

    private static final String CAUSE = "Cause";
    private static final String SYSTEM_ERROR_DETAIL = "System error detail";

    @Test
    void testDefaultConstructor() {
        SystemException exception = new SystemException();
        assertNull(exception.getMessage());
        assertNull(exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "System error";
        SystemException exception = new SystemException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessageAndErrorCode() {
        String message = "System error";
        Integer errorCode = 500;
        SystemException exception = new SystemException(message, errorCode);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessageAndThrowable() {
        String message = "System error";
        Throwable cause = new Throwable(CAUSE);
        SystemException exception = new SystemException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailTitleAndErrorCode() {
        String title = "System Error Title";
        Integer errorCode = 500;
        SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, title, errorCode);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
    }

    @Test
    void testConstructorWithDetailTitleAndThrowable() {
        String title = "System Error Title";
        Throwable cause = new Throwable(CAUSE);
        SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, title, cause);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(500, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailErrorCodeAndThrowable() {
        Integer errorCode = 500;
        Throwable cause = new Throwable(CAUSE);
        SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, errorCode, cause);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailTitleErrorCodeAndThrowable() {
        String title = "System Error Title";
        Integer errorCode = 500;
        Throwable cause = new Throwable(CAUSE);
        SystemException exception = new SystemException(SYSTEM_ERROR_DETAIL, title, errorCode, cause);
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(SYSTEM_ERROR_DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }
}