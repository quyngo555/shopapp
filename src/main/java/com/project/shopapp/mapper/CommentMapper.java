package com.project.shopapp.mapper;

import com.project.shopapp.dto.response.CommentResponse;
import com.project.shopapp.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "product.id", target = "productId")
    CommentResponse toCommentResponse(Comment comment);
}
