package com.audition.web.advice;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

class ExceptionControllerAdviceTest {

    @Mock
    private transient AuditionLogger logger;

    @InjectMocks
    private transient ExceptionControllerAdvice exceptionControllerAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleHttpClientException() {
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        final ProblemDetail problemDetail = exceptionControllerAdvice.handleHttpClientException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("400 Bad Request", problemDetail.getDetail());
        assertEquals(ExceptionControllerAdvice.DEFAULT_TITLE, problemDetail.getTitle());
    }

    @Test
    void testHandleMainException() {
        final Exception exception = new Exception("Unexpected error");
        final ProblemDetail problemDetail = exceptionControllerAdvice.handleMainException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Unexpected error", problemDetail.getDetail());
        assertEquals(ExceptionControllerAdvice.DEFAULT_TITLE, problemDetail.getTitle());
        verify(logger).logErrorWithException(any(), eq("An unexpected error occurred"), eq(exception));
    }

    @Test
    void testHandleSystemException() {
        final SystemException exception = new SystemException("System error", "System Error Title", 500);
        final ProblemDetail problemDetail = exceptionControllerAdvice.handleSystemException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("System error", problemDetail.getDetail());
        assertEquals("System Error Title", problemDetail.getTitle());
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        final HttpHeaders headers = new HttpHeaders();
        final HttpStatusCode status = HttpStatus.BAD_REQUEST;
        final WebRequest request = mock(WebRequest.class);

        final ResponseEntity<Object> responseEntity = exceptionControllerAdvice.handleMethodArgumentNotValid(exception,
            headers, status, request);

        assert responseEntity != null;
        final ProblemDetail problemDetail = (ProblemDetail) responseEntity.getBody();
        assert problemDetail != null;
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals(ExceptionControllerAdvice.DEFAULT_TITLE, problemDetail.getTitle());
    }
}