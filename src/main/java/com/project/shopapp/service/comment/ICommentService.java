package com.project.shopapp.service.comment;

import com.project.shopapp.dto.request.CommentRequest;
import com.project.shopapp.dto.response.CommentResponse;

import java.util.List;

public interface ICommentService {
    CommentResponse insertComment(CommentRequest request);

    void deleteComment(Long commentId);
    CommentResponse updateComment(Long id, CommentRequest request) ;

    List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId);
    List<CommentResponse> getCommentsByProduct(Long productId);
    void generateFakeComments() throws Exception;
}
