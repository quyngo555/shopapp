package com.project.shopapp.service.impl;

import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.CategoryResponse;
import com.project.shopapp.dto.response.ProductResponse;
import com.project.shopapp.entity.Product;
import com.project.shopapp.enums.MessageKeys;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.ProductMapper;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.ICategoryService;
import com.project.shopapp.service.IProductService;
import com.project.shopapp.utils.LocalizationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService implements IProductService {
    ICategoryService categoryService;
    ProductRepository productRepository;
    ProductMapper productMapper;
    LocalizationUtils localizationUtils;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        CategoryResponse category = categoryService.getCategoryById(request.getCategoryId());
        Product product = productMapper.toProduct(request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED, id)
        );
        return productMapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<Product> products = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return products.map(productMapper::toProductResponse);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id).orElseThrow(


                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        productMapper.updateProduct(product, request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public String deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(productRepository::delete);
        return "Delete product successfully!";
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public List<ProductResponse> findProductsByIds(String ids) {
        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return productRepository.findProductsByIds(productIds).stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
