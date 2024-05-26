package com.project.shopapp.repository;

import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    @Query("SELECT o FROM User o WHERE o.active = true AND (:keyword IS NULL OR :keyword = '' OR " +
                  "o.fullName LIKE %:keyword% " +
                  "OR o.address LIKE %:keyword% " +
                  "OR o.phoneNumber LIKE %:keyword%) ")
    Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id IN :userIds")
    List<User> findProductsByIds(@Param("userIds") List<Long> userIds);
}
