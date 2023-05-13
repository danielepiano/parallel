package com.dp.spring.parallel.hestia.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.util.Set;

public interface HeadquartersReceptionistUserRepository extends SoftDeleteJpaRepository<HeadquartersReceptionistUser, Integer> {

    Set<HeadquartersReceptionistUser> findAllByHeadquarters(Headquarters headquarters);

}
