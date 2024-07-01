package com.project.shopapp.dto.response;

import com.project.shopapp.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Long id;

//    Order order;

    Product product;

    Float price;

//    @JsonProperty("number_of_products")
    int numberOfProducts;

//    @JsonProperty("total_money")
    Float totalMoney;

    String color;
}
