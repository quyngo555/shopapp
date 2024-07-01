package com.project.shopapp.controller;

import com.nimbusds.jose.JOSEException;
import com.project.shopapp.dto.request.AuthenticationRequest;
import com.project.shopapp.dto.request.IntrospectRequest;
import com.project.shopapp.dto.request.RefreshTokenRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.auth.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    IAuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request,
                                    HttpServletRequest httpServletRequest){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(authenticationService.authenticate(request, httpServletRequest))
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> authenticate(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(result)
                .build());
    }

    @PostMapping("/logout")
    ResponseEntity<?> logout(@RequestBody IntrospectRequest request)  {
        authenticationService.logout(request);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message("Logout successfully!")
                .build());
    }

}
