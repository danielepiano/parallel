package com.dp.spring.parallel.hestia.database.repositories;

import com.dp.spring.parallel.hestia.database.entities.AdminUser;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

public interface AdminUserRepository extends SoftDeleteJpaRepository<AdminUser, Integer> {
}
