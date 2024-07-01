package com.project.shopapp.enums;

import lombok.Getter;
@Getter
public enum MessageKeys {

    LOGIN_SUCCESSFULLY("user.login.login_successfully"),
    USER_IS_LOCKED("user.login.user_is_locked"),
    UPDATE_CATEGORY_SUCCESSFULLY("category.update_category.update_successfully"),
    DELETE_ORDER_SUCCESSFULLY("order.delete_order.delete_successfully"),
    DELETE_ORDER_DETAIL_SUCCESSFULLY("order.delete_order_detail.delete_successfully"),
    UPLOAD_IMAGES_MAX_5("product.upload_images.error_max_5_images"),
    UPLOAD_IMAGES_FILE_LARGE("product.upload_images.file_large"),
    UPLOAD_IMAGES_FILE_MUST_BE_IMAGE("product.upload_images.file_must_be_image"),
    ROLE_DOES_NOT_EXISTS("user.login.role_not_exist"),
    UNCATEGORIZED_EXCEPTION("exception.uncategorized"),
    USER_EXISTED("user.register.existed"),
    USER_NOT_EXISTED("user.login.not_existed"),
    UNAUTHENTICATED("auth.unauthenticated"),
    UNAUTHORIZED("auth.unauthorized"),
    TOKEN_NOT_EXISTED("request.token.not_existed"),
    REFRESH_TOKEN_NOT_EXISTED("request.refresh_token.not_existed"),
    CATEGORY_NOT_EXISTED("category.category_not_existed"),
    PRODUCT_NOT_EXISTED("product.not_existed"),
    PRODUCT_IMAGE_NOT_EXISTED("product_image.not_existed"),
    ORDERDETAIL_NOT_EXISTED("order_detail.get_order_detail.not_existed"),
    ORDER_SHIPPINGDATE_INVALID("order.shipping_date.set_shippingdate_failed"),
    ORDER_NOT_EXISTED("order.get_order.not_found"),
    COUPON_NOT_EXISTED("coupon.get_coupon.not_existed"),
    COUPON_NOT_ACTIVE("coupon.get_coupon.not_active"),
    COMMENT_NOT_EXISTED("comment.get_comment.not_existed"),
    COMMENT_UPDATE_FAILED("comment.update_comment.update_failed")
    ;


    MessageKeys(String messageKey) {
        this.messageKey = messageKey;
    }

    private final String messageKey;
}
