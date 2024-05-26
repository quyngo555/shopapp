package com.project.shopapp.service;

import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId, int page, int limit);
    ProductResponse updateProduct(Long id, ProductRequest request);
    String deleteProduct(Long id);
    boolean existsByName(String name);

    List<ProductResponse> findProductsByIds(String ids);

}
