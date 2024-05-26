package com.project.shopapp.service.impl;

import com.project.shopapp.dto.request.UserCreationRequest;
import com.project.shopapp.dto.response.UserResponse;
import com.project.shopapp.entity.Role;
import com.project.shopapp.entity.User;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.UserMapper;
import com.project.shopapp.repository.RoleRepository;
import com.project.shopapp.repository.TokenRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    RoleRepository roleRepository;
    TokenRepository tokenRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (!request.getPhoneNumber().isBlank() && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if (!request.getEmail().isBlank() && userRepository.existsByEmail(request.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        User user = userMapper.toUser(request);
        Set<com.project.shopapp.entity.Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(com.project.shopapp.enums.Role.USER);
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    @Override
    public UserResponse getUserDetailsFromToken(){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public String resetPassword(Long userId, String newPassword) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);
        userRepository.save(existingUser);
        tokenRepository.findByUser(existingUser).stream()
                .forEach(tokenRepository::delete);
        return "Reset password successfully!";
    }

    @Override
    @Transactional
    public String blockOrEnable(Long userId, Boolean active) {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        existingUser.setActive(active);
        userRepository.save(existingUser);
        return "Enable/block user successfully!";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> findAll(String keyword, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        return userRepository.findAll(keyword, pageRequest).map(userMapper::toUserResponse);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void grantAuthority(String ids) {
        List<Long> userIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Role roleAdmin = roleRepository.findByName(com.project.shopapp.enums.Role.ADMIN);
        userRepository.findProductsByIds(userIds).stream()
                .forEach(user -> {
                    user.getRoles().add(roleAdmin);
                    userRepository.save(user);
                });
    }

}
