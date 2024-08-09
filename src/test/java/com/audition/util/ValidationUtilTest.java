package com.audition.util;

import static com.audition.Constants.ErrorTitles.INVALID_PARAMETER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.audition.common.exception.SystemException;
import org.junit.jupiter.api.Test;

class ValidationUtilTest {

    @Test
    void givenInputWhenEmptyErrorIsThrown() {
        final String errorMessage = "ERROR_MESSAGE";
        final SystemException exception = assertThrows(SystemException.class,
            () -> ValidationUtil.isNoneEmpty("", errorMessage));
        assertEquals(exception.getDetail(), INVALID_PARAMETER_ERROR);
        assertEquals(exception.getTitle(), errorMessage);
    }

}