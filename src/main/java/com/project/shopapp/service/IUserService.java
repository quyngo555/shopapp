package com.project.shopapp.service;

import com.project.shopapp.dto.request.UserCreationRequest;
import com.project.shopapp.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse getUserDetailsFromToken();
    String resetPassword(Long userId, String newPassword);
    String blockOrEnable(Long userId, Boolean active);
    Page<UserResponse> findAll(String keyword, int page, int limit);
    void grantAuthority(String userIds);
}
