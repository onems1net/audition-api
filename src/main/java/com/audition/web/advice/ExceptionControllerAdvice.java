package com.audition.web.advice;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    public static final String DEFAULT_TITLE = "API Error Occurred";
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";

    private final transient AuditionLogger logger;

    @Autowired
    public ExceptionControllerAdvice(final AuditionLogger logger) {
        super();
        this.logger = logger;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    ProblemDetail handleHttpClientException(final HttpClientErrorException e) {
        return createProblemDetail(e, e.getStatusCode());

    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleMainException(final Exception e) {
        // Log the exception
        logger.logErrorWithException(LOG, "An unexpected error occurred", e);

        // Get the HTTP status code from the exception
        final HttpStatusCode status = getHttpStatusCodeFromException(e);

        // Create and return the ProblemDetail
        return createProblemDetail(e, status);
    }

    @ExceptionHandler(SystemException.class)
    ProblemDetail handleSystemException(final SystemException e) {
        // Log the exception
        logger.logErrorWithException(LOG, "SystemException occurred", e);

        final HttpStatusCode status = getHttpStatusCodeFromSystemException(e);
        return createProblemDetail(e, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final @NonNull MethodArgumentNotValidException ex,
        final @NonNull HttpHeaders headers, final @NonNull HttpStatusCode status, final @NonNull WebRequest request) {
        final ProblemDetail problemDetail = createProblemDetail(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(problemDetail, headers, status);
    }

    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        if (exception instanceof SystemException) {
            problemDetail.setTitle(((SystemException) exception).getTitle());
        } else {
            problemDetail.setTitle(DEFAULT_TITLE);
        }
        return problemDetail;
    }

    private String getMessageFromException(final Exception exception) {
        if (StringUtils.isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    private HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        try {
            return HttpStatusCode.valueOf(exception.getStatusCode());
        } catch (final IllegalArgumentException iae) {
            logger.info(LOG, ERROR_MESSAGE + exception.getStatusCode());
            return INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) exception).getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return METHOD_NOT_ALLOWED;
        }
        return INTERNAL_SERVER_ERROR;
    }
}



