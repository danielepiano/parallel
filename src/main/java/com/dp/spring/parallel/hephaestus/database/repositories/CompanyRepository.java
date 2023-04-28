package com.dp.spring.parallel.hephaestus.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

public interface CompanyRepository extends SoftDeleteJpaRepository<Company, Integer> {
}
