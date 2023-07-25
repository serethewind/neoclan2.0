package com.neoclan.identitymanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "token_database")

public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private Boolean expired;
    private Boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
