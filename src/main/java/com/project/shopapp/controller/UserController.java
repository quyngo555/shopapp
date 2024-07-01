package com.project.shopapp.controller;

import com.project.shopapp.dto.request.UserCreationRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.user.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    IUserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreationRequest request){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(userService.createUser(request))
                .build());
    }

    @PutMapping("/reset-password/{userId}")
    public ResponseEntity<?> resetPassword(@PathVariable long userId, @RequestParam String newPassword){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message(userService.resetPassword(userId, newPassword))
                .build());
    }

    @PutMapping("/block/{userId}/{active}")
    public ResponseEntity<?> blockOrEnable(
            @Valid @PathVariable long userId,
            @Valid @PathVariable int active
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message(userService.blockOrEnable(userId, active > 0))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUser(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(userService.findAll(keyword, page, limit))
                .build());
    }

    @PostMapping("/grant-authority")
    public ResponseEntity<?> grantAuthority(@RequestParam String ids){
        userService.grantAuthority(ids);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message("grant authority successfully!")
                .build());
    }

    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo(){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(userService.getUserDetailsFromToken())
                .build());
    }

}
