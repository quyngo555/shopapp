package com.project.shopapp.service.impl;

import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.CategoryResponse;
import com.project.shopapp.dto.response.ProductResponse;
import com.project.shopapp.entity.Product;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.mapper.ProductMapper;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.ICategoryService;
import com.project.shopapp.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class ProductService implements IProductService {
    ICategoryService categoryService;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        CategoryResponse category = categoryService.getCategoryById(request.getCategoryId());
        Product product = productMapper.toProduct(request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Product not found with id: " + id)
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
                () -> new DataNotFoundException("Product not found with id: " + id)
        );
        productMapper.updateProduct(product, request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(productRepository::delete);
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