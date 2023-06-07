package com.dp.spring.parallel.hephaestus.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.util.Optional;
import java.util.Set;

public interface WorkspaceRepository extends SoftDeleteJpaRepository<Workspace, Integer> {

    Optional<Workspace> findByIdAndHeadquarters(Integer id, Headquarters headquarters);

    Set<Workspace> findAllByHeadquarters(Headquarters headquarters);

    boolean existsByNameAndHeadquarters(String name, Headquarters headquarters);

    boolean existsByIdNotAndNameAndHeadquarters(Integer id, String name, Headquarters headquarters);

}
