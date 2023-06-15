package com.dp.spring.parallel.mnemosyne.api.controllers.impl;

import com.dp.spring.parallel.mnemosyne.api.controllers.WorkplaceBookingController;
import com.dp.spring.parallel.mnemosyne.api.dtos.UserWorkplaceBookingResponseDTO;
import com.dp.spring.parallel.mnemosyne.api.dtos.WorkplaceBookingRequestDTO;
import com.dp.spring.parallel.mnemosyne.api.dtos.WorkplaceBookingResponseDTO;
import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;
import com.dp.spring.parallel.mnemosyne.services.WorkplaceBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequiredArgsConstructor
public class WorkplaceBookingControllerImpl implements WorkplaceBookingController {
    private final WorkplaceBookingService workplaceBookingService;


    @Override
    public List<UserWorkplaceBookingResponseDTO> userWorkplacesBookingsFromDate(LocalDate fromDate) {
        return this.workplaceBookingService.workplaceBookingsFromDate(ofNullable(fromDate).orElse(LocalDate.now()))
                .stream()
                .map(UserWorkplaceBookingResponseDTO::of)
                .toList();
    }

    @Override
    public WorkplaceBookingResponseDTO bookWorkplace(Integer workspaceId, Integer workplaceId, WorkplaceBookingRequestDTO bookRequest) {
        final WorkplaceBooking booking = this.workplaceBookingService.book(workspaceId, workplaceId, bookRequest);
        return WorkplaceBookingResponseDTO.of(booking);
    }

    @Override
    public WorkplaceBookingResponseDTO setParticipation(Integer workspaceId, Integer workplaceId, Integer bookingId) {
        final WorkplaceBooking booking = this.workplaceBookingService.setParticipation(workspaceId, workplaceId, bookingId);
        return WorkplaceBookingResponseDTO.of(booking);
    }

    @Override
    public void cancelBooking(Integer workspaceId, Integer workplaceId, Integer bookingId) {
        this.workplaceBookingService.cancel(workspaceId, workplaceId, bookingId);
    }

}
