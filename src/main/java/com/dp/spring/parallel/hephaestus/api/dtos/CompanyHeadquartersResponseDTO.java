package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyHeadquartersResponseDTO {
    Integer id;
    Integer companyId;

    String city;
    String address;
    String phoneNumber;

    String description;


    public static CompanyHeadquartersResponseDTO of(final Headquarters headquarters) {
        return CompanyHeadquartersResponseDTO.builder()
                .id(headquarters.getId())
                .companyId(headquarters.getCompany().getId())
                .city(headquarters.getCity())
                .address(headquarters.getAddress())
                .phoneNumber(headquarters.getPhoneNumber())
                .description(headquarters.getDescription())
                .build();
    }
}
