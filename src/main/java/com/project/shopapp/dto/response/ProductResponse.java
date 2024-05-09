package com.project.shopapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entity.ProductImage;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse extends BaseResponse {
    Long id;
    String name;
    Float price;
    String thumbnail;
    String description;

    @JsonProperty("category_id")
    Long categoryId;

    @JsonProperty("product_images")
    List<ProductImage> productImages = new ArrayList<>();
}
