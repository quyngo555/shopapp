package com.project.shopapp.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
//    @JsonProperty("id")
    Long id;

//    @JsonProperty("fullname")
    String fullName;

//    @JsonProperty("phone_number")
    String phoneNumber;

//    @JsonProperty("address")
    String address;

//    @JsonProperty("is_active")
    boolean active;

//    @JsonProperty("date_of_birth")
    Date dateOfBirth;

//    @JsonProperty("facebook_account_id")
    int facebookAccountId;

//    @JsonProperty("google_account_id")
    int googleAccountId;
//    @JsonProperty("roles")
    Set<RoleResponse> roles;
}
