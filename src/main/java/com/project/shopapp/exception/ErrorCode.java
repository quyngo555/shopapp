package com.project.shopapp.exception;

import com.project.shopapp.enums.MessageKeys;
import com.project.shopapp.utils.LocalizationUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, MessageKeys.UNCATEGORIZED_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAM(1001, MessageKeys.UNCATEGORIZED_EXCEPTION, HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, MessageKeys.USER_EXISTED, HttpStatus.BAD_REQUEST),
//    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
//    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, MessageKeys.USER_NOT_EXISTED, HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, MessageKeys.UNAUTHENTICATED, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, MessageKeys.UNAUTHORIZED, HttpStatus.FORBIDDEN),
//    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_EXISTED(1009, MessageKeys.TOKEN_NOT_EXISTED, HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_EXISTED(1010, MessageKeys.REFRESH_TOKEN_NOT_EXISTED, HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1011, MessageKeys.CATEGORY_NOT_EXISTED, HttpStatus.BAD_REQUEST),
//    PRODUCT_NOT_EXISTED(1012, MessageKeys.PRODUCT_NOT_EXISTED, HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1012, MessageKeys.PRODUCT_NOT_EXISTED, HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_NOT_EXISTED(1013, MessageKeys.PRODUCT_IMAGE_NOT_EXISTED, HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_UPLOAD_IMAGES_MAX_5(1014, MessageKeys.UPLOAD_IMAGES_MAX_5, HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_UPLOAD_IMAGES_FILE_LARGE(1015, MessageKeys.UPLOAD_IMAGES_FILE_LARGE, HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, MessageKeys message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private MessageKeys message;
    private HttpStatusCode statusCode;
}
