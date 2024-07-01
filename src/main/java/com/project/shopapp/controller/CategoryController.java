package com.project.shopapp.controller;

import com.project.shopapp.dto.request.CategoryRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.category.ICategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    ICategoryService categoryService;
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(categoryService.createCategory(request))
                .build());
    }
    @GetMapping
    public ResponseEntity<?> getAllCategory(){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(categoryService.getAllCategories())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") long categoryId){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(categoryService.getCategoryById(categoryId))
                .build());
    }

    @PutMapping("/id")
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") long categoryId,
            @RequestBody @Valid CategoryRequest request){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(categoryService.updateCategory(categoryId, request))
                .build());
    }
}
