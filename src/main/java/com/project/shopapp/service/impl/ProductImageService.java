package com.project.shopapp.service.impl;

import com.project.shopapp.dto.response.ProductImageResponse;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.ProductImageMapper;
import com.project.shopapp.repository.ProductImageRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.IProductImageService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageService implements IProductImageService {
    ProductImageRepository productImageRepository;
    ProductImageMapper productImageMapper;
    ProductRepository productRepository;

    static String UPLOADS_FOLDER = "uploads";

    @Override
    @Transactional
    public void deleteProductImage(Long id) throws IOException {
        ProductImage productImage = productImageRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_EXISTED)
        );
        productImageRepository.delete(productImage);
        String filename = productImage.getImageUrl();
        // Đường dẫn đến thư mục chứa file
        java.nio.file.Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Đường dẫn đầy đủ đến file cần xóa
        java.nio.file.Path filePath = uploadDir.resolve(filename);

        // Kiểm tra xem file tồn tại hay không
        if (Files.exists(filePath)) {
            // Xóa file
            Files.delete(filePath);
        } else {
            throw new FileNotFoundException("File not found: " + filename);
        }
    }


    @Override
    @Transactional
    public List<ProductImageResponse> createProductImage(long productId, List<MultipartFile> files) throws Exception {
        Product existingProduct = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        files = files == null ? new ArrayList<MultipartFile>() : files;
        if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new AppException(ErrorCode.PRODUCT_IMAGE_UPLOAD_IMAGES_MAX_5);
        }
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if(file.getSize() == 0) {
                continue;
            }
            // Kiểm tra kích thước file và định dạng
            if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                throw new AppException(ErrorCode.PRODUCT_IMAGE_UPLOAD_IMAGES_FILE_LARGE);
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) {
                throw new Exception("File must be an image");
            }
            // Lưu file và cập nhật thumbnail trong DTO
//            String filename = productService.storeFile(file); // Thay thế hàm này với code của bạn để lưu file
            String filename = storeFile(file);
            //lưu vào đối tượng product trong DB
            ProductImage newProductImage = ProductImage.builder()
                    .product(existingProduct)
                    .imageUrl(filename)
                    .build();
            int size = productImageRepository.findByProductId(productId).size();
            if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                throw new RuntimeException(
                        "Number of images must be <= "
                                +ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
            }
            if (existingProduct.getThumbnail() == null ) {
                existingProduct.setThumbnail(newProductImage.getImageUrl());
            }

            productImages.add(newProductImage);
            existingProduct.setProductImages(productImages);
            productRepository.save(existingProduct);
            return productImages.stream()
                    .map(productImageMapper::toProductImageResponse)
                    .toList();
        }

        return null;
    }

    @Override
    public UrlResource getProductImage(String imageName) throws Exception {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new AppException(ErrorCode.PRODUCT_IMAGE_NOT_EXISTED);
            }
        } catch (Exception e) {
            throw new Exception("Error occurred while retrieving image: " + e.getMessage());
        }
    }


    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        //String uniqueFilename = UUID.randomUUID().toString() + "_" + filename; //old code, not good
        String uniqueFilename = UUID.randomUUID() + "_" + System.nanoTime(); // Convert nanoseconds to microseconds
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
