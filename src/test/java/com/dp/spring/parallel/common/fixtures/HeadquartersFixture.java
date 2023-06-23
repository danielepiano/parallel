package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hestia.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class HeadquartersFixture {

    public static Headquarters get() {
        List<User> observers = new ArrayList<>();
        return new Headquarters()
                .setCompany(CompanyFixture.get())
                .setCity("city-test")
                .setAddress("address-test")
                .setDescription("description-test")
                .setPhoneNumber("number-test")
                .setObservers(observers);
    }

    public static Headquarters getWithObservers() {
        List<User> observers = new ArrayList<>();
        observers.add(UserFixture.getCompanyManager());
        return new Headquarters()
                .setCompany(CompanyFixture.get())
                .setCity("city-test")
                .setAddress("address-test")
                .setDescription("description-test")
                .setPhoneNumber("number-test")
                .setObservers(observers);
    }

}
