package com.dp.spring.parallel.mnemosyne.database.repositories;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventBookingRepository extends SoftDeleteJpaRepository<EventBooking, Integer> {

    Optional<EventBooking> findByIdAndEvent(Integer id, Event event);

    List<EventBooking> findAllByWorkerAndEventOnDateGreaterThanEqualOrderByEventOnDate(User worker, LocalDate fromDate);

    List<EventBooking> findAllByEvent(Event event);

    boolean existsByWorkerAndEvent(User worker, Event event);

    long countByEvent(Event event);

    /*
    List<WorkplaceBooking> findAllByWorkerAndBookingDateGreaterThanEqual(User worker, LocalDate fromDate, Sort sort);

    Optional<WorkplaceBooking> findByIdAndWorkplace(Integer id, Workplace workplace);

    Set<WorkplaceBooking> findAllByWorkplace(Workplace workplace);

    @Query("select b.worker from WorkplaceBooking b" +
            " where b.workplace.workspace.headquarters = ?1 and b.bookingDate = ?2 and b.active = true")
    Set<User> findAllWorkersBookedByHeadquartersAndBookingDate(Headquarters headquarters, LocalDate bookingDate);

    long countAllByWorkerAndBookingDate(User worker, LocalDate bookingDate);

    long countAllByWorkplaceAndBookingDate(Workplace workplace, LocalDate bookingDate);*/

}
