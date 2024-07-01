package com.project.shopapp.controller;

import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.productImage.IProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/product_images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageController {
    IProductImageService productImageService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) throws IOException {
        productImageService.deleteProductImage(id);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(null)
                .message(String.format("ProductImage with id = %d deleted successfully", id))
                .build());
    }


}
