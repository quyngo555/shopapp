package com.project.shopapp.controller;

import com.project.shopapp.dto.request.CategoryRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.dto.response.CategoryResponse;
import com.project.shopapp.service.impl.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(request))
                .build();
    }
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategory(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable("id") long categoryId){
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryById(categoryId))
                .build();
    }

    @PutMapping("/id")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable("id") long categoryId,
            @RequestBody @Valid CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.updateCategory(categoryId, request))
                .build();
    }




}
