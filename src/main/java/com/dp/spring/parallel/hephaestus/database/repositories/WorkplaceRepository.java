package com.dp.spring.parallel.hephaestus.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WorkplaceRepository extends SoftDeleteJpaRepository<Workplace, Integer> {

    Optional<Workplace> findByIdAndWorkspace(Integer id, Workspace workspace);

    Set<Workplace> findAllByWorkspace(Workspace workspace);

    List<Workplace> findAllByWorkspace(Workspace workspace, Sort sort);

    @Query("select wp_av from Workplace wp_av where wp_av.workspace = ?1 " +
            "and wp_av.id not in (" +
            "select distinct wp.id from Workplace wp join WorkplaceBooking b on b.workplace = wp " +
            "where b.bookingDate = ?2 and wp.workspace = ?1 and wp.active = true and wp.active = true " +
            ") order by wp_av.name, wp_av.description asc")
    List<Workplace> findAllAvailableByWorkspaceAndBookingDate(Workspace workspace, LocalDate bookingDate);

    boolean existsByNameAndWorkspace(String name, Workspace workspace);

    boolean existsByIdNotAndNameAndWorkspace(Integer id, String name, Workspace workspace);

    long countByWorkspaceHeadquarters(Headquarters headquarters);

    @Query("select count(wp) from Workplace wp join WorkplaceBooking b on b.workplace = wp" +
            " where b.bookingDate = ?2 and wp.workspace.headquarters = ?1" +
            " and b.active = true and wp.active = true")
    long countNotAvailableByWorkspaceHeadquartersAndBookingDate(Headquarters headquarters, LocalDate bookingDate);

    long countByWorkspace(Workspace workspace);

    @Query("select count(wp) from Workplace wp join WorkplaceBooking b on b.workplace = wp" +
            " where b.bookingDate = ?2 and wp.workspace = ?1" +
            " and b.active = true and wp.active = true")
    long countNotAvailableByWorkspaceAndBookingDate(Workspace workspace, LocalDate bookingDate);

}
