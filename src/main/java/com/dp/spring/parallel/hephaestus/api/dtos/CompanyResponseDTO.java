package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponseDTO {
    Integer id;

    String name;
    String city;
    String address;
    String phoneNumber;

    String description;
    String websiteUrl;


    public static CompanyResponseDTO of(final Company company) {
        return CompanyResponseDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .city(company.getCity())
                .address(company.getAddress())
                .phoneNumber(company.getPhoneNumber())
                .description(company.getDescription())
                .websiteUrl(company.getWebsiteUrl())
                .build();
    }
}
