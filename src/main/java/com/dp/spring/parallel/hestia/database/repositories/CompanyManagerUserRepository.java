package com.dp.spring.parallel.hestia.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.util.Set;

public interface CompanyManagerUserRepository extends SoftDeleteJpaRepository<CompanyManagerUser, Integer> {

    Set<CompanyManagerUser> findAllByCompany(Company company);

}
