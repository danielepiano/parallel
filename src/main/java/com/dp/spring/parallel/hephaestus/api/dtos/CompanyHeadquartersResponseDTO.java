package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyHeadquartersResponseDTO {
    Integer id;
    Integer companyId;

    String city;
    String address;
    String phoneNumber;

    String description;

    long totalWorkplaces;


    public static CompanyHeadquartersResponseDTO of(final long totalWorkplaces, final Headquarters headquarters) {
        return CompanyHeadquartersResponseDTO.builder()
                .id(headquarters.getId())
                .companyId(headquarters.getCompany().getId())
                .city(headquarters.getCity())
                .address(headquarters.getAddress())
                .phoneNumber(headquarters.getPhoneNumber())
                .description(headquarters.getDescription())
                .totalWorkplaces(totalWorkplaces)
                .build();
    }
}
