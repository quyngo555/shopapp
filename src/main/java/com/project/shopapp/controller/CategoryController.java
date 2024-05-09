package com.project.shopapp.controller;

import com.project.shopapp.dto.request.CategoryRequest;
import com.project.shopapp.dto.response.ResponseObject;
import com.project.shopapp.service.impl.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request){
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(categoryService.createCategory(request))
                .build());
    }
    @GetMapping
    public ResponseEntity<?> getAllCategory(){
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(categoryService.getAllCategories())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") long categoryId){
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(categoryService.getCategoryById(categoryId))
                .build());
    }

    @PutMapping("/id")
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") long categoryId,
            @RequestBody @Valid CategoryRequest request){
        return ResponseEntity.ok().body(ResponseObject.builder()
                .data(categoryService.updateCategory(categoryId, request))
                .build());
    }
}
