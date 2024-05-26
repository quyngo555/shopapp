package com.project.shopapp.repository;

import com.project.shopapp.entity.Token;
import com.project.shopapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);
    Optional<Token> findByToken(String token);
    Optional<Token> findByRefreshToken(String token);
}
