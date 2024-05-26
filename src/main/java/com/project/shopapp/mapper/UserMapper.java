package com.project.shopapp.mapper;

import com.project.shopapp.dto.request.UserCreationRequest;
import com.project.shopapp.dto.response.UserResponse;
import com.project.shopapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);
//    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(User user);
}
