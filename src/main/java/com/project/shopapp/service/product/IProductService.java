package com.project.shopapp.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.ProductListResponse;
import com.project.shopapp.dto.response.ProductResponse;

import java.util.List;

public interface IProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    ProductListResponse getAllProducts(String keyword,
                                       Long categoryId, int page, int limit) throws JsonProcessingException;
    ProductResponse updateProduct(Long id, ProductRequest request);
    String deleteProduct(Long id);
    boolean existsByName(String name);

    List<ProductResponse> findProductsByIds(String ids);

    ProductResponse likeProduct(Long productId);
    List<ProductResponse> findFavoriteProductsByUserId();
    void generateFakeLikes();

}
