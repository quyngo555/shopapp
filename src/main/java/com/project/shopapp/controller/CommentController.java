package com.project.shopapp.controller;

import com.project.shopapp.dto.request.CommentRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.dto.response.CommentResponse;
import com.project.shopapp.service.comment.ICommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    ICommentService commentService;
    @GetMapping("")
    public ResponseEntity<?> getAllComments(
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam("product_id") Long productId
    ){
        List<CommentResponse> commentResponses;
        if (userId == null) {
            commentResponses = commentService.getCommentsByProduct(productId);
        } else {
            commentResponses = commentService.getCommentsByUserAndProduct(userId, productId);
        }
        return ResponseEntity.ok().body(ApiResponse.builder()
                        .data(commentResponses)
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(commentService.updateComment(commentId, request))
                .build());
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> insertComment(
            @Valid @RequestBody CommentRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(commentService.insertComment(request))
                .build());
    }

    @PostMapping("/generateFakeComments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> generateFakeComments() throws Exception {
        commentService.generateFakeComments();
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Insert fake comments succcessfully")
                .data(null)
                .build());
    }
}
