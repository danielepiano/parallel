package com.dp.spring.parallel.mnemosyne.api.dtos;

import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkplaceBookingResponseDTO {

    Integer id;
    Integer workplaceId;
    LocalDate bookingDate;
    LocalDate bookedOn;
    boolean present;


    public static WorkplaceBookingResponseDTO of(final WorkplaceBooking booking) {
        return WorkplaceBookingResponseDTO.builder()
                .id(booking.getId())
                .workplaceId(booking.getWorkplace().getId())
                .bookingDate(booking.getBookingDate())
                .bookedOn(Instant.ofEpochMilli(booking.getCreatedDate()).atZone(ZoneId.systemDefault()).toLocalDate())
                .present(booking.isPresent())
                .build();
    }

}