package com.project.shopapp.controller;

import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.coupon.ICouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponController {
    ICouponService couponService;

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateCouponValue(
            @RequestParam("couponCode") String couponCode,
            @RequestParam("totalAmount") double totalAmount) {
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(couponService.calculateCouponValue(couponCode, totalAmount))
                .build()
        );
    }
}
