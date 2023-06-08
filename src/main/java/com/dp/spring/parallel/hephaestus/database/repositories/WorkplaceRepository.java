package com.dp.spring.parallel.hephaestus.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WorkplaceRepository extends SoftDeleteJpaRepository<Workplace, Integer> {

    Optional<Workplace> findByIdAndWorkspace(Integer id, Workspace workspace);

    Set<Workplace> findAllByWorkspace(Workspace workspace);

    List<Workplace> findAllByWorkspace(Workspace workspace, Sort sort);

    boolean existsByNameAndWorkspace(String name, Workspace workspace);

    boolean existsByIdNotAndNameAndWorkspace(Integer id, String name, Workspace workspace);

}
