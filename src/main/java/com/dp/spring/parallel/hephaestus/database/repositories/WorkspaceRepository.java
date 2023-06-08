package com.dp.spring.parallel.hephaestus.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WorkspaceRepository extends SoftDeleteJpaRepository<Workspace, Integer> {

    Optional<Workspace> findByIdAndHeadquarters(Integer id, Headquarters headquarters);

    Set<Workspace> findAllByHeadquarters(Headquarters headquarters);

    List<Workspace> findAllByHeadquarters(Headquarters headquarters, Sort sort);

    boolean existsByNameAndHeadquarters(String name, Headquarters headquarters);

    boolean existsByIdNotAndNameAndHeadquarters(Integer id, String name, Headquarters headquarters);

}
