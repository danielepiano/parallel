package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.hephaestus.database.entities.Company;

public class CompanyFixture {

    public static Company get() {
        return new Company()
                .setName("company-test")
                .setDescription("description-test")
                .setCity("city-test")
                .setAddress("address-test")
                .setPhoneNumber("number-test")
                .setWebsiteUrl("url-test");
    }

}
