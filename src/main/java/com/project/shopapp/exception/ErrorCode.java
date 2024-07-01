package com.project.shopapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "exception.uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAM(1001, "exception.uncategorized", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "user.register.existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "user.login.not_existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "auth.unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "auth.unauthorized", HttpStatus.FORBIDDEN),
//    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_EXISTED(1009, "request.token.not_existed", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_EXISTED(1010, "request.refresh_token.not_existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1011, "category.category_not_existed", HttpStatus.BAD_REQUEST),
//    PRODUCT_NOT_EXISTED(1012, MessageKeys.PRODUCT_NOT_EXISTED, HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1012, "product.not_existed", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_NOT_EXISTED(1013, "product_image.not_existed", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_UPLOAD_IMAGES_MAX_5(1014, "product.upload_images.error_max_5_images", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_UPLOAD_IMAGES_FILE_LARGE(1015, "product.upload_images.file_large", HttpStatus.BAD_REQUEST),
    ORDER_DETAIL_NOT_EXISTED(1016, "order_detail.get_order_detail.not_existed", HttpStatus.BAD_REQUEST),
    ORDER_SHIPPING_DATE_INVALID(1017, "order.shipping_date.set_shippingdate_failed", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(1019, "order.get_order.not_found", HttpStatus.BAD_REQUEST),
    COUPON_NOT_EXISTED(1020, "coupon.get_coupon.not_existed", HttpStatus.BAD_REQUEST),
    COUPON_NOT_ACTIVE(1021, "coupon.get_coupon.not_active", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EXISTED(1022, "comment.get_comment.not_existed", HttpStatus.BAD_REQUEST),
    COMMENT_UPDATE_FAILED(1023, "comment.update_comment.update_failed", HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
