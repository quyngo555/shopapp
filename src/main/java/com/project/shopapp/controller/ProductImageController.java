package com.project.shopapp.controller;

import com.project.shopapp.dto.response.ResponseObject;
import com.project.shopapp.service.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/product_images")
@RequiredArgsConstructor
public class ProductImageController {
    private final IProductImageService productImageService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) throws IOException {
        productImageService.deleteProductImage(id);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(null)
                .message(String.format("ProductImage with id = %d deleted successfully", id))
                .build());
    }


}
