package com.dp.spring.parallel.hestia.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.util.Set;

public interface EmployeeUserRepository extends SoftDeleteJpaRepository<EmployeeUser, Integer> {

    Set<EmployeeUser> findAllByCompany(Company company);

}
