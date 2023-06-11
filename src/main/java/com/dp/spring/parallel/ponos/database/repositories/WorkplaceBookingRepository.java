package com.dp.spring.parallel.ponos.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.ponos.database.entities.WorkplaceBooking;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WorkplaceBookingRepository extends SoftDeleteJpaRepository<WorkplaceBooking, Integer> {

    List<WorkplaceBooking> findAllByWorkerAndBookingDateGreaterThanEqual(User worker, LocalDate fromDate, Sort sort);

    Optional<WorkplaceBooking> findByIdAndWorkplace(Integer id, Workplace workplace);

    Set<WorkplaceBooking> findAllByWorkplace(Workplace workplace);

    long countAllByWorkerAndBookingDate(User worker, LocalDate bookingDate);

    long countAllByWorkplaceAndBookingDate(Workplace workplace, LocalDate bookingDate);

}
