package com.project.shopapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.dto.response.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @JsonProperty("user_id")
    Long userId;
    @JsonProperty("fullname")
    String fullName;

    String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, message = "Phone number must be at least 5 characters")
    String phoneNumber;

    String status;

    String address;

    String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    Float totalMoney;

    @JsonProperty("shipping_method")
    String shippingMethod;

    @JsonProperty("shipping_address")
    @NotNull(message = "Shipping address is mandatory")
    String shippingAddress;

    @JsonProperty("shipping_date")
    Date shippingDate;

    @JsonProperty("payment_method")
    String paymentMethod;

    @JsonProperty("cart_items")
    List<CartItem> cardItems;
    @JsonProperty("coupon_code")
    private String couponCode;
}
