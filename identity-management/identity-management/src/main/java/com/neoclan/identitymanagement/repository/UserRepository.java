package com.neoclan.identitymanagement.repository;

import com.neoclan.identitymanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);
}
