package com.project.shopapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "token", length = 500)
    String token;

    @Column(name = "refresh_token", length = 50)
    String refreshToken;

    @Column(name = "token_type", length = 50)
    String tokenType;

    @Column(name = "expiration_date")
    Date expirationDate;

    @Column(name = "refresh_expiration_date")
    Date refreshExpirationDate;

    @Column(name = "is_mobile", columnDefinition = "TINYINT(1)")
    boolean isMobile;

    boolean revoked;
    boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}

