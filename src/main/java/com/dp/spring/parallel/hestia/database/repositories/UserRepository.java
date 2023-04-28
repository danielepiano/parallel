package com.dp.spring.parallel.hestia.database.repositories;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.util.Optional;

public interface UserRepository<T extends User> extends SoftDeleteJpaRepository<T, Integer> {
    Optional<T> findByEmail(String email);
}
