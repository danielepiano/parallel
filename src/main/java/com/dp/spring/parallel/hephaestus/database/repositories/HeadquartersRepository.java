package com.dp.spring.parallel.hephaestus.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.util.Optional;
import java.util.Set;

public interface HeadquartersRepository extends SoftDeleteJpaRepository<Headquarters, Integer> {

    Optional<Headquarters> findByIdAndCompany(Integer id, Company company);

    Set<Headquarters> findAllByCompany(Company company);

    boolean existsByIdAndCompanyId(Integer id, Integer companyId);

    boolean existsByCityAndAddressAndCompany(String city, String address, Company company);

    boolean existsByIdNotAndCityAndAddressAndCompany(Integer id, String city, String address, Company company);
}
