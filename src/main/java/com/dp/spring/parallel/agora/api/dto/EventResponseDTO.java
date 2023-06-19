package com.dp.spring.parallel.agora.api.dto;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponseDTO {

    Integer id;

    CompanyInfoDTO company;
    HeadquartersInfoDTO headquarters;

    String name;
    LocalDate eventDate;
    LocalTime startTime;
    LocalTime endTime;

    Long availablePlaces;
    Long totalPlaces;

    Boolean alreadyBooked;


    public static EventResponseDTO of(final Event event) {
        return EventResponseDTO.of(null, null, event);
    }

    public static EventResponseDTO of(final Boolean alreadyBooked, final Long availablePlaces, final Event event) {
        return EventResponseDTO.builder()
                .id(event.getId())
                .company(CompanyInfoDTO.of(event.getHeadquarters().getCompany()))
                .headquarters(HeadquartersInfoDTO.of(event.getHeadquarters()))
                .name(event.getName())
                .availablePlaces(availablePlaces)
                .totalPlaces(event.getMaxPlaces().longValue())
                .eventDate(event.getOnDate())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .alreadyBooked(alreadyBooked)
                .build();
    }


    @Jacksonized
    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CompanyInfoDTO {
        Integer id;
        String name;

        static EventResponseDTO.CompanyInfoDTO of(final Company company) {
            return EventResponseDTO.CompanyInfoDTO.builder()
                    .id(company.getId())
                    .name(company.getName())
                    .build();
        }
    }

    @Jacksonized
    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class HeadquartersInfoDTO {
        Integer id;
        String city;
        String address;

        static EventResponseDTO.HeadquartersInfoDTO of(final Headquarters headquarters) {
            return EventResponseDTO.HeadquartersInfoDTO.builder()
                    .id(headquarters.getId())
                    .city(headquarters.getCity())
                    .address(headquarters.getAddress())
                    .build();
        }
    }

}
