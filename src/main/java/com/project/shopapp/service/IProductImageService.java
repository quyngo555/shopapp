package com.project.shopapp.service;

import com.project.shopapp.dto.response.ProductImageResponse;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductImageService {
    void deleteProductImage(Long id) throws IOException;
    List<ProductImageResponse> createProductImage(long productId, List<MultipartFile> files) throws Exception;
    UrlResource getProductImage(String imageName) throws Exception;
}
