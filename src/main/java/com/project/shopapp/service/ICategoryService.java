package com.project.shopapp.service;

import com.project.shopapp.dto.request.CategoryRequest;
import com.project.shopapp.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(long id);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(long categoryId, CategoryRequest request);
    CategoryResponse deleteCategory(long id) throws Exception;
}
