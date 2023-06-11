package com.dp.spring.parallel.ponos.api.controllers.impl;

import com.dp.spring.parallel.ponos.api.controllers.WorkplaceBookingController;
import com.dp.spring.parallel.ponos.api.dtos.UserWorkplaceBookingDTO;
import com.dp.spring.parallel.ponos.api.dtos.WorkplaceBookingDTO;
import com.dp.spring.parallel.ponos.api.dtos.WorkplaceBookingRequestDTO;
import com.dp.spring.parallel.ponos.database.entities.WorkplaceBooking;
import com.dp.spring.parallel.ponos.services.WorkplaceBookingService;
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
    public List<UserWorkplaceBookingDTO> userWorkplacesBookingsFromDate(LocalDate fromDate) {
        return this.workplaceBookingService.workplaceBookingsFromDate(ofNullable(fromDate).orElse(LocalDate.now()))
                .stream()
                .map(UserWorkplaceBookingDTO::of)
                .toList();
    }

    @Override
    public WorkplaceBookingDTO bookWorkplace(Integer workspaceId, Integer workplaceId, WorkplaceBookingRequestDTO bookRequest) {
        final WorkplaceBooking booking = this.workplaceBookingService.book(workspaceId, workplaceId, bookRequest);
        return WorkplaceBookingDTO.of(booking);
    }

    @Override
    public WorkplaceBookingDTO setParticipation(Integer workspaceId, Integer workplaceId, Integer bookingId) {
        final WorkplaceBooking booking = this.workplaceBookingService.setParticipation(workspaceId, workplaceId, bookingId);
        return WorkplaceBookingDTO.of(booking);
    }

    @Override
    public void cancelBooking(Integer workspaceId, Integer workplaceId, Integer bookingId) {
        this.workplaceBookingService.cancel(workspaceId, workplaceId, bookingId);
    }

}
