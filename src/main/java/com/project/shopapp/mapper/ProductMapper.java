package com.project.shopapp.mapper;

import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.ProductResponse;
import com.project.shopapp.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")

    ProductResponse toProductResponse(Product product);
    Product toProduct(ProductRequest request);
    void updateProduct(@MappingTarget Product product, ProductRequest request);
}
