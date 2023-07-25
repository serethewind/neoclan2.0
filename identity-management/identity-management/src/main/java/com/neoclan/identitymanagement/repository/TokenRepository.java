package com.neoclan.identitymanagement.repository;

import com.neoclan.identitymanagement.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

//    @Query("select _ from _tokens _ where _.user.id = ?1 and _.expired = false and _.revoked = false")
//    List<TokenEntity> fetchAllValidTokensByUser(Long id);

    @Query("select t from TokenEntity t where t.user.id = ?1 and t.expired = false and t.revoked = false")
    List<TokenEntity> findAllValidTokensByUser(Long id);

    Optional<TokenEntity> findByToken(String token);
}
