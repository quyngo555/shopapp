package com.project.shopapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
//    @JsonProperty("phone_number")
//    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

//    @Min(value = 1, message = "You must enter role's Id")
//    @JsonProperty("role_id")
//    private Long roleId;
}

