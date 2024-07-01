package com.project.shopapp.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    boolean authenticated;
//    @JsonProperty("token")
    String token;

//    @JsonProperty("refresh_token")
    String refreshToken;
    String tokenType = "Bearer";
}
