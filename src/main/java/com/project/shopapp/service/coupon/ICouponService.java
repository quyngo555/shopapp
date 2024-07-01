package com.project.shopapp.service.coupon;

import com.project.shopapp.dto.response.CouponResponse;

public interface ICouponService {
    CouponResponse calculateCouponValue(String couponCode, double totalAmount);
}
