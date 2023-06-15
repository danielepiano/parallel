package com.dp.spring.parallel.mnemosyne.api.dtos;

import com.dp.spring.parallel.agora.database.entities.Event;
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
public class EventBookingResponseDTO {

    Integer id;
    EventInfoDTO event;
    LocalDate bookedOn;


    public static EventBookingResponseDTO of(final EventBooking booking) {
        return EventBookingResponseDTO.builder()
                .id(booking.getId())
                .event(EventInfoDTO.of(booking.getEvent()))
                .bookedOn(Instant.ofEpochMilli(booking.getCreatedDate()).atZone(ZoneId.systemDefault()).toLocalDate())
                .build();
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

        Long totalPlaces;

        static EventInfoDTO of(final Event event) {
            return EventInfoDTO.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .startTime(event.getStartTime())
                    .endTime(event.getEndTime())
                    .totalPlaces(event.getMaxPlaces().longValue())
                    .build();
        }
    }

}