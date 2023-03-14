package com.dp.spring.parallel.hestia.database.repositories;

import com.dp.spring.parallel.hestia.database.entities.TokenDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenDetailsRepository extends JpaRepository<TokenDetails, Integer> {
    Optional<TokenDetails> findByToken(String token);




    default List<TokenDetails> findAllValidTokensByUserId(Integer userId) {
        return this.findAllByUserIdAndRevoked(userId, false);
    }

    List<TokenDetails> findAllByUserIdAndRevoked(Integer userId, boolean revoked);
}
