package com.audition.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class SystemExceptionTest {

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
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailTitleAndErrorCode() {
        String detail = "System error detail";
        String title = "System Error Title";
        Integer errorCode = 500;
        SystemException exception = new SystemException(detail, title, errorCode);
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
    }

    @Test
    void testConstructorWithDetailTitleAndThrowable() {
        String detail = "System error detail";
        String title = "System Error Title";
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(detail, title, cause);
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(500, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailErrorCodeAndThrowable() {
        String detail = "System error detail";
        Integer errorCode = 500;
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(detail, errorCode, cause);
        assertEquals(detail, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithDetailTitleErrorCodeAndThrowable() {
        String detail = "System error detail";
        String title = "System Error Title";
        Integer errorCode = 500;
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(detail, title, errorCode, cause);
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }
}
