package com.dp.spring.parallel.agora.database.repositories;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.springcore.database.repositories.SoftDeleteJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends SoftDeleteJpaRepository<Event, Integer> {

    Optional<Event> findByIdAndHeadquarters(Integer id, Headquarters headquarters);

    @Query("select e from Event e where e.active = true" +
            " order by e.onDate, e.startTime, e.endTime")
    List<Event> findAllByHeadquarters(Headquarters headquarters);

    @Query("select e from Event e where e.onDate > ?1 and e.active = true" +
            " order by e.onDate, e.startTime, e.endTime")
    List<Event> findAllByEventDateGreaterThanEqual(LocalDate fromDate);

}
