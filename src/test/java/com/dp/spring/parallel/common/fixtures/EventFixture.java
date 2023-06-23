package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.agora.database.entities.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventFixture {

    public static Event get() {
        return new Event()
                .setHeadquarters(HeadquartersFixture.get())
                .setName("event")
                .setOnDate(LocalDate.now())
                .setStartTime(LocalTime.now())
                .setEndTime(LocalTime.now().plusHours(2))
                .setMaxPlaces(30);
    }

}
