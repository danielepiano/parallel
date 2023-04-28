package com.dp.spring.parallel.hephaestus.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.util.Optional;

public interface HeadquartersRepository extends SoftDeleteJpaRepository<Headquarters, Integer> {

    Optional<Headquarters> findByIdAndCompany(Integer id, Company company);

    boolean existsByIdAndCompanyId(Integer id, Integer companyId);

}
