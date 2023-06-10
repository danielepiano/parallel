package com.dp.spring.parallel.ponos.database.repositories;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.ponos.database.entities.WorkplaceBooking;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkplaceBookingRepository extends SoftDeleteJpaRepository<WorkplaceBooking, Integer> {

    Optional<WorkplaceBooking> findByIdAndWorkplace(Integer id, Workplace workplace);

    long countAllByWorkerAndBookingDate(User worker, LocalDate bookingDate);

    long countAllByWorkplaceAndBookingDate(Workplace workplace, LocalDate bookingDate);

}
