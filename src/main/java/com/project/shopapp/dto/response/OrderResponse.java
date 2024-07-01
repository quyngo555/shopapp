package com.project.shopapp.dto.response;

import com.project.shopapp.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    User user;
    String fullName;
    String email;
    String phoneNumber;
    String address;
    String note;
    Date orderDate;
    String status;
    Float totalMoney;
    String shippingMethod;
    String shippingAddress;
    Date shippingDate;
    String trackingNumber;
    String paymentMethod;
    Boolean active;
    List<OrderDetailResponse> orderDetails;
}
