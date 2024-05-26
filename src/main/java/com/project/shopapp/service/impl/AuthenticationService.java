package com.project.shopapp.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.shopapp.dto.request.AuthenticationRequest;
import com.project.shopapp.dto.request.IntrospectRequest;
import com.project.shopapp.dto.request.RefreshTokenRequest;
import com.project.shopapp.dto.response.AuthenticationResponse;
import com.project.shopapp.dto.response.IntrospectResponse;
import com.project.shopapp.entity.Token;
import com.project.shopapp.entity.User;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.TokenRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.service.IAuthenticationService;
import com.project.shopapp.utils.LocalizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {
    int MAX_TOKENS = 3;

    UserRepository userRepository;
    TokenRepository tokenRepository;
    LocalizationUtils localizationUtils;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @Transactional
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest httpServletRequest){
        String userAgent = httpServletRequest.getHeader("User-Agent");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with subject: " + request.getEmail()));

        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword());

        if (!authenticated)
            throw new BadCredentialsException("Unauthenticated");

        var token = generateToken(user);
        var refreshToken = UUID.randomUUID().toString();
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();
        if (tokenCount >= MAX_TOKENS) {
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }

        tokenRepository.save(Token.builder()
                        .tokenType("Bearer")
                        .isMobile(isMobileDevice(userAgent))
                        .token(token)
                        .revoked(false)
                        .expired(false)
                        .user(user)
                        .refreshToken(refreshToken)
                        .expirationDate(new Date(
                                Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                        ))
                        .refreshExpirationDate(new Date(
                                Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                        ))
                        .build());

        return AuthenticationResponse.builder()
                .tokenType("Bearer")
                .token(token)
                .authenticated(true)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("shopapp")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    @Transactional
    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (Exception e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());


        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        tokenRepository.findByToken(token).orElseThrow(
                () -> new AppException(ErrorCode.TOKEN_NOT_EXISTED)

        );
        return signedJWT;
    }
    @Transactional
    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {

        Token existingToken = tokenRepository.findByRefreshToken(request.getRefreshToken()).orElseThrow(
                () -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_EXISTED)
        );
        if(!existingToken.getRefreshExpirationDate().after(new Date()))
            throw new RuntimeException("Token is expired");

        var token = generateToken(existingToken.getUser());
        var refreshToken = UUID.randomUUID().toString();
        existingToken.setRefreshToken(refreshToken);
        existingToken.setToken(token);
        existingToken.setExpirationDate(new Date(
                Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
        ));
        existingToken.setExpirationDate(new Date(
                Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()
        ));
        tokenRepository.save(existingToken);

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }
    @Transactional
    @Override
    public void logout(IntrospectRequest request) {
        try {
            verifyToken(request.getToken());

            Token existingToken = tokenRepository.findByToken(request.getToken()).orElseThrow(
                () -> new AppException(ErrorCode.TOKEN_NOT_EXISTED)
            );
            tokenRepository.delete(existingToken);


        } catch (Exception exception){
            log.info("Internal error server");
        }
    }

     boolean isMobileDevice(String userAgent) {
        return userAgent.toLowerCase().contains("mobile");
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
            });

        return stringJoiner.toString();
    }
}
