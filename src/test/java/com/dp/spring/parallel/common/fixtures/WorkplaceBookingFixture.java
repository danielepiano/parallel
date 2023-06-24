package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;

import java.time.LocalDate;

public class WorkplaceBookingFixture {

    public static WorkplaceBooking getFuture() {
        return new WorkplaceBooking()
                .setWorker(UserFixture.getCompanyManager())
                .setWorkplace(WorkplaceFixture.get())
                .setBookingDate(LocalDate.now().plusDays(2))
                .setPresent(false);
    }

    public static WorkplaceBooking get() {
        return new WorkplaceBooking()
                .setWorker(UserFixture.getCompanyManager())
                .setWorkplace(WorkplaceFixture.get())
                .setBookingDate(LocalDate.now())
                .setPresent(false);
    }

    public static WorkplaceBooking getPast() {
        return new WorkplaceBooking()
                .setWorker(UserFixture.getCompanyManager())
                .setWorkplace(WorkplaceFixture.get())
                .setBookingDate(LocalDate.now().minusDays(2))
                .setPresent(false);
    }

}
