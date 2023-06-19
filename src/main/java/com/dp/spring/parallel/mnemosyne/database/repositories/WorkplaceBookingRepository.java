package com.dp.spring.parallel.mnemosyne.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WorkplaceBookingRepository extends SoftDeleteJpaRepository<WorkplaceBooking, Integer> {

    List<WorkplaceBooking> findAllByWorkerAndBookingDateGreaterThanEqual(User worker, LocalDate fromDate, Sort sort);

    @Query("select b from WorkplaceBooking b where b.workplace.workspace.headquarters = ?1 and b.bookingDate = ?2" +
            " and b.active = true order by b.present desc, b.worker.firstName asc, b.worker.lastName asc")
    List<WorkplaceBooking> findAllByHeadquartersAndBookingDate(Headquarters headquarters, LocalDate bookingDate);

    Optional<WorkplaceBooking> findByIdAndWorkplace(Integer id, Workplace workplace);

    Set<WorkplaceBooking> findAllByWorkplace(Workplace workplace);

    @Query("select b.worker from WorkplaceBooking b" +
            " where b.workplace.workspace.headquarters = ?1 and b.bookingDate = ?2 and b.active = true")
    Set<User> findAllWorkersBookedByHeadquartersAndBookingDate(Headquarters headquarters, LocalDate bookingDate);

    long countAllByWorkerAndBookingDate(User worker, LocalDate bookingDate);

    long countAllByWorkplaceAndBookingDate(Workplace workplace, LocalDate bookingDate);

}
