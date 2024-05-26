package com.project.shopapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    boolean authenticated;
    @JsonProperty("token")
    String token;

    @JsonProperty("refresh_token")
    String refreshToken;
    String tokenType = "Bearer";
}
