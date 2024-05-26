package com.project.shopapp.mapper;

import com.project.shopapp.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleResponse {
    com.project.shopapp.dto.response.RoleResponse toRoleResponse(Role role);
}
