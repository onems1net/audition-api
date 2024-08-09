package com.audition.util;

import com.audition.common.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class ValidationUtil {

    public static void isNoneEmpty(
        final String parameter,
        final String errorMessage) {
        if (StringUtils.isEmpty(parameter)) {
            throw new SystemException(
                "Invalid parameters provided",
                errorMessage,
                HttpStatus.BAD_REQUEST.value()
            );
        }
    }

}
