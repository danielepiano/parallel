package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadquartersResponseDTO {
    Integer id;
    CompanyResponseDTO company;

    String city;
    String address;
    String phoneNumber;

    String description;

    long totalWorkplaces;

    Boolean favorite;


    public static HeadquartersResponseDTO of(final long totalWorkplaces, final Headquarters headquarters) {
        return HeadquartersResponseDTO.builder()
                .id(headquarters.getId())
                .company(CompanyResponseDTO.of(headquarters.getCompany()))
                .city(headquarters.getCity())
                .address(headquarters.getAddress())
                .phoneNumber(headquarters.getPhoneNumber())
                .description(headquarters.getDescription())
                .totalWorkplaces(totalWorkplaces)
                .build();
    }

    public static HeadquartersResponseDTO favorite(final long totalWorkplaces, final Headquarters headquarters) {
        return HeadquartersResponseDTO.builder()
                .id(headquarters.getId())
                .company(CompanyResponseDTO.of(headquarters.getCompany()))
                .city(headquarters.getCity())
                .address(headquarters.getAddress())
                .phoneNumber(headquarters.getPhoneNumber())
                .description(headquarters.getDescription())
                .totalWorkplaces(totalWorkplaces)
                .favorite(true)
                .build();
    }

    public static HeadquartersResponseDTO nonFavorite(final long totalWorkplaces, final Headquarters headquarters) {
        return HeadquartersResponseDTO.builder()
                .id(headquarters.getId())
                .company(CompanyResponseDTO.of(headquarters.getCompany()))
                .city(headquarters.getCity())
                .address(headquarters.getAddress())
                .phoneNumber(headquarters.getPhoneNumber())
                .description(headquarters.getDescription())
                .totalWorkplaces(totalWorkplaces)
                .favorite(false)
                .build();
    }
}
