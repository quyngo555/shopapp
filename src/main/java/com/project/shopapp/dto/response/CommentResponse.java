package com.project.shopapp.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse extends BaseResponse {
    Long id;
    String content;
    UserResponse user;
    Long productId;
}
