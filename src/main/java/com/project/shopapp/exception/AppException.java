package com.project.shopapp.exception;

import com.project.shopapp.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AppException extends RuntimeException {

    public AppException(ErrorCode errorCode, Object... parameters) {
        super(String.valueOf(errorCode.getMessage()));
        this.errorCode = errorCode;
        this.params = Arrays.stream(parameters)
                .map(Object::toString)
                .collect(Collectors.joining(", "));;
    }
    private String params;

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getParams() {
        return params;
    }
}
