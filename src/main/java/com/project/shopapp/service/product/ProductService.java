package com.project.shopapp.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.CategoryResponse;
import com.project.shopapp.dto.response.ProductListResponse;
import com.project.shopapp.dto.response.ProductResponse;
import com.project.shopapp.dto.response.UserResponse;
import com.project.shopapp.entity.Favorite;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.User;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.ProductMapper;
import com.project.shopapp.repository.FavoriteRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.service.category.ICategoryService;
import com.project.shopapp.service.user.IUserService;
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
    UserRepository userRepository;
    IUserService userService;
    IProductRedisService productRedisService;
    ICategoryService categoryService;
    ProductRepository productRepository;
    ProductMapper productMapper;
    FavoriteRepository favoriteRepository;

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
    public ProductListResponse getAllProducts(String keyword, Long categoryId, int page, int limit) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        log.info(String.format("keyword = %s, category_id = %d, page = %d, limit = %d",
                keyword, categoryId, page, limit));
//        productRedisService.clear();
        ProductListResponse productListResponse = productRedisService
                .getAllProducts(keyword, categoryId, pageRequest);
        if(productListResponse == null){
            Page<ProductResponse> products = productRepository
                    .searchProducts(categoryId, keyword, pageRequest)
                    .map(productMapper::toProductResponse);
            productListResponse = ProductListResponse.builder()
                    .totalPages(products.getTotalPages())
                    .products(products.getContent())
                    .build();

            productRedisService.saveAllProducts(
                    productListResponse,
                    keyword,
                    categoryId,
                    pageRequest
            );
        }


        return productListResponse;
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

    @Override
    @Transactional
    public ProductResponse likeProduct(Long productId) {
        UserResponse userLogin = userService.getUserDetailsFromToken();

        User user = userRepository.findById(userLogin.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Optional<Favorite> favorite = favoriteRepository.findByUserIdAndProductId(userLogin.getId(), productId);
        if(favorite.isPresent()){
            favoriteRepository.delete(favorite.get());
        }else{
            favoriteRepository.save(Favorite.builder()
                            .user(user)
                            .product(product)
                            .build());
        }
        return productMapper.toProductResponse(productRepository.findById(productId).orElseThrow(null));
    }



    @Override
    @Transactional
    public List<ProductResponse> findFavoriteProductsByUserId() {
        UserResponse userLogin = userService.getUserDetailsFromToken();

        return productRepository.findFavoriteProductsByUserId(userLogin.getId()).stream()
                .map(productMapper::toProductResponse)
                .toList();

    }

    @Override
    public void generateFakeLikes() {

    }
}
