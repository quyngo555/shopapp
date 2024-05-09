package com.project.shopapp.dto.response;

import com.project.shopapp.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {
    Long id;
    Product product;
    String imageUrl;
}
