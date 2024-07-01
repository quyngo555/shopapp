package com.project.shopapp.service.comment;

import com.github.javafaker.Faker;
import com.project.shopapp.dto.request.CommentRequest;
import com.project.shopapp.dto.response.CommentResponse;
import com.project.shopapp.dto.response.UserResponse;
import com.project.shopapp.entity.Comment;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.User;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.CommentMapper;
import com.project.shopapp.repository.CommentRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.service.user.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService implements ICommentService {
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    UserRepository userRepository;
    ProductRepository productRepository;
    IUserService userService;
    @Override
    @Transactional
    public CommentResponse insertComment(CommentRequest request) {
        UserResponse userLogin = userService.getUserDetailsFromToken();
        if(userLogin.getId() != request.getUserId()){
            throw new AppException(ErrorCode.COMMENT_UPDATE_FAILED);
        }
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Comment comment = Comment.builder()
                .user(user)
                .product(product)
                .content(request.getContent())
                .build();


        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest request) {
        UserResponse user = userService.getUserDetailsFromToken();
        if(user.getId() != request.getUserId()){
            throw new AppException(ErrorCode.COMMENT_UPDATE_FAILED);
        }
        Comment existingComment = commentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        existingComment.setContent(request.getContent());
        return commentMapper.toCommentResponse(commentRepository.save(existingComment));
    }

    @Override
    public List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId) {
        return commentRepository.findByUserIdAndProductId(userId, productId).stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    @Override
    public List<CommentResponse> getCommentsByProduct(Long productId) {
        return commentRepository.findByProductId(productId).stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    @Override
    public void generateFakeComments() throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Comment> comments = new ArrayList<>();
        final int totalRecords = 10_000;
        final int batchSize = 1000;
        for (int i = 0; i < totalRecords; i++) {

            // Select a random user and product
            User user = users.get(random.nextInt(users.size()));
            Product product = products.get(random.nextInt(products.size()));

            // Generate a fake comment
            Comment comment = Comment.builder()
                    .user(user)
                    .product(product)
                    .content(faker.lorem().sentence())
                    .build();

            // Set a random created date within the range of 2015 to now
            LocalDateTime startDate = LocalDateTime.of(2015, 1, 1, 0, 0);
            LocalDateTime endDate = LocalDateTime.now();
            long randomEpoch = ThreadLocalRandom.current()
                    .nextLong(startDate.toEpochSecond(ZoneOffset.UTC), endDate.toEpochSecond(ZoneOffset.UTC));
            comment.setCreatedAt(LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC));
            // Save the comment
            comments.add(comment);
            if(comments.size() >= batchSize) {
                commentRepository.saveAll(comments);
                comments.clear();
            }
        }
    }
}
