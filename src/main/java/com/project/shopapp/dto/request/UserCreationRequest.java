package com.project.shopapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @JsonProperty("fullname")
    String fullName;


    @JsonProperty("phone_number")
    String phoneNumber = "";

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    @Email(message = "Please type email format")
    String email ;

    String address ;

    @NotBlank(message = "Password cannot be blank")
    String password ;

    @JsonProperty("date_of_birth")
    Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    int facebookAccountId;

    @JsonProperty("google_account_id")
    int googleAccountId;


}
