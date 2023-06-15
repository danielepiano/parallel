package com.dp.spring.parallel.mnemosyne.api.dtos;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEventBookingResponseDTO {

    Integer id;
    CompanyInfoDTO company;
    HeadquartersInfoDTO headquarters;
    EventInfoDTO event;

    LocalDate bookedOn;


    public static UserEventBookingResponseDTO of(final Long eventAvailablePlaces, final EventBooking booking) {
        return UserEventBookingResponseDTO.builder()
                .id(booking.getId())
                .company(CompanyInfoDTO.of(booking.getEvent().getHeadquarters().getCompany()))
                .headquarters(HeadquartersInfoDTO.of(booking.getEvent().getHeadquarters()))
                .event(EventInfoDTO.of(eventAvailablePlaces, booking.getEvent()))
                .bookedOn(Instant.ofEpochMilli(booking.getCreatedDate()).atZone(ZoneId.systemDefault()).toLocalDate())
                .build();
    }

    @Jacksonized
    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CompanyInfoDTO {
        Integer id;
        String name;

        static CompanyInfoDTO of(final Company company) {
            return CompanyInfoDTO.builder()
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

        static HeadquartersInfoDTO of(final Headquarters headquarters) {
            return HeadquartersInfoDTO.builder()
                    .id(headquarters.getId())
                    .city(headquarters.getCity())
                    .address(headquarters.getAddress())
                    .build();
        }
    }

    @Jacksonized
    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class EventInfoDTO {
        Integer id;
        String name;
        LocalDate onDate;
        LocalTime startTime;
        LocalTime endTime;

        Long availablePlaces;
        Long totalPlaces;

        static EventInfoDTO of(final Long availablePlaces, final Event event) {
            return EventInfoDTO.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .onDate(event.getOnDate())
                    .startTime(event.getStartTime())
                    .endTime(event.getEndTime())
                    .availablePlaces(availablePlaces)
                    .totalPlaces(event.getMaxPlaces().longValue())
                    .build();
        }
    }

}