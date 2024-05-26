package com.project.shopapp.service;

import com.nimbusds.jose.JOSEException;
import com.project.shopapp.dto.request.AuthenticationRequest;
import com.project.shopapp.dto.request.IntrospectRequest;
import com.project.shopapp.dto.request.RefreshTokenRequest;
import com.project.shopapp.dto.response.AuthenticationResponse;
import com.project.shopapp.dto.response.IntrospectResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest httpServletRequest);
    IntrospectResponse introspect(IntrospectRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
    void logout(IntrospectRequest request);
}
