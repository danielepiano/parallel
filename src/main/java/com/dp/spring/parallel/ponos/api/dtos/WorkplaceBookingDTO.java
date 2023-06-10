package com.dp.spring.parallel.ponos.api.dtos;

import com.dp.spring.parallel.ponos.database.entities.WorkplaceBooking;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkplaceBookingDTO {

    Integer workplaceId;
    LocalDate bookingDate;
    LocalDate bookedOn;
    boolean present;


    public static WorkplaceBookingDTO of(final WorkplaceBooking booking) {
        return WorkplaceBookingDTO.builder()
                .workplaceId(booking.getWorkplace().getId())
                .bookingDate(booking.getBookingDate())
                .bookedOn(Instant.ofEpochMilli(booking.getCreatedDate()).atZone(ZoneId.systemDefault()).toLocalDate())
                .present(booking.isPresent())
                .build();
    }

}