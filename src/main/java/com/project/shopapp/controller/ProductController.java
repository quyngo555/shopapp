package com.project.shopapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.productImage.IProductImageService;
import com.project.shopapp.service.product.IProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    IProductService productService;
    IProductImageService productImageService;
    @PostMapping("")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest request){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(productService.createProduct(request))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws JsonProcessingException {
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(productService.getAllProducts(keyword, categoryId, page, limit))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id){

        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(productService.getProductById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProductRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(productService.updateProduct(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message(String.format("Product with id = %d deleted successfully", id))
                .build());
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?>  getProductsByIds(@RequestParam("ids") String ids){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(productService.findProductsByIds(ids))
                .build());
    }

    @PostMapping(value = "/{id}/uploads",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) throws Exception{
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(productImageService.createProductImage(productId, files))
                .build());
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(productImageService.getProductImage(imageName));
    }

    @PostMapping("/like/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> likeProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .data(productService.likeProduct(productId))
                .message("Like product successfully")
                .build());
    }

    @PostMapping("/favorite-products")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> findFavoriteProductsByUserId() throws Exception {
        return ResponseEntity.ok(ApiResponse.builder()
                .data(productService.findFavoriteProductsByUserId())
                .message("Favorite products retrieved successfully")
                .build());
    }

}
