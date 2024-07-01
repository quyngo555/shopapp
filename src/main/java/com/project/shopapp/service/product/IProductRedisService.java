package com.project.shopapp.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dto.response.ProductListResponse;
import org.springframework.data.domain.PageRequest;

public interface IProductRedisService {
    void clear();
    ProductListResponse getAllProducts(
            String keyword,
            Long categoryId, PageRequest pageRequest) throws JsonProcessingException;
    void saveAllProducts(ProductListResponse productListResponse,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest) throws JsonProcessingException;
}
