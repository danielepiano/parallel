package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.agora.database.entities.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventFixture {

    public static Event get() {
        return new Event()
                .setHeadquarters(HeadquartersFixture.getWithObservers())
                .setName("event")
                .setOnDate(LocalDate.now().plusDays(2))
                .setStartTime(LocalTime.now())
                .setEndTime(LocalTime.now().plusHours(2))
                .setMaxPlaces(30);
    }

    public static Event getPast() {
        return new Event()
                .setHeadquarters(HeadquartersFixture.getWithObservers())
                .setName("event")
                .setOnDate(LocalDate.now().minusYears(3))
                .setStartTime(LocalTime.now())
                .setEndTime(LocalTime.now().plusHours(2))
                .setMaxPlaces(30);
    }

}
