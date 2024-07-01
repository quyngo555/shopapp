package com.project.shopapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "coupon_conditions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    Coupon coupon;

    @Column(name = "attribute", nullable = false)
    String attribute;

    @Column(name = "operator", nullable = false)
    String operator;

    @Column(name = "value", nullable = false)
    String value;

    @Column(name = "discount_amount", nullable = false)
    BigDecimal discountAmount;
}
