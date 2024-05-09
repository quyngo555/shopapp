package com.project.shopapp.mapper;

import com.project.shopapp.dto.response.ProductImageResponse;
import com.project.shopapp.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    @Mapping(target = "product", ignore = true)
    ProductImageResponse toProductImageResponse(ProductImage productImage);

}
