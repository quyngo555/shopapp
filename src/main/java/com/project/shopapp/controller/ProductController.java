package com.project.shopapp.controller;

import com.project.shopapp.dto.request.ProductRequest;
import com.project.shopapp.dto.response.ResponseObject;
import com.project.shopapp.service.IProductImageService;
import com.project.shopapp.service.IProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(productService.createProduct(request))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(productService.getAllProducts(keyword, categoryId, page, limit))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id){

        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(productService.getProductById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProductRequest request
    ){
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(productService.updateProduct(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(null)
                .message(String.format("Product with id = %d deleted successfully", id))
                .build());
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?>  getProductsByIds(@RequestParam("ids") String ids){
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(productService.findProductsByIds(ids))
                .build());
    }

    @PostMapping(value = "/{id}/uploads",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) throws Exception{
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(productImageService.createProductImage(productId, files))
                .build());
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(productImageService.getProductImage(imageName));
    }

}
