package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;

public class EventBookingFixture {

    public static EventBooking get() {
        return new EventBooking()
                .setEvent(EventFixture.get())
                .setWorker(UserFixture.getCompanyManager());
    }

    public static EventBooking getPast() {
        return new EventBooking()
                .setEvent(EventFixture.getPast())
                .setWorker(UserFixture.getCompanyManager());
    }

}
