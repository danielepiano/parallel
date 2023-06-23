package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;

public class HeadquartersFixture {

    public static Headquarters get() {
        return new Headquarters()
                .setCompany(CompanyFixture.get())
                .setCity("city-test")
                .setAddress("address-test")
                .setDescription("description-test")
                .setPhoneNumber("number-test");
    }

}
