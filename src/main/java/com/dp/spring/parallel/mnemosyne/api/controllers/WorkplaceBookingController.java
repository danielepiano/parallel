package com.dp.spring.parallel.mnemosyne.api.controllers;

import com.dp.spring.parallel.mnemosyne.api.dtos.UserWorkplaceBookingResponseDTO;
import com.dp.spring.parallel.mnemosyne.api.dtos.WorkplaceBookingRequestDTO;
import com.dp.spring.parallel.mnemosyne.api.dtos.WorkplaceBookingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.*;

@RequestMapping("/api/v1")
public interface WorkplaceBookingController {

    @GetMapping(path = "/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    List<UserWorkplaceBookingResponseDTO> userWorkplacesBookingsFromDate(
            @RequestParam(name = "fromDate", required = false) LocalDate fromDate
    );

    @PostMapping(
            path = "/workspaces/{workspaceId}/workplaces/{workplaceId}/bookings",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    WorkplaceBookingResponseDTO bookWorkplace(
            @PathVariable("workspaceId") Integer workspaceId,
            @PathVariable("workplaceId") Integer workplaceId,
            @Valid @RequestBody WorkplaceBookingRequestDTO bookRequest
    );

    @PatchMapping(
            path = "/workspaces/{workspaceId}/workplaces/{workplaceId}/bookings/{bookingId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({ROLE_ADMIN_VALUE, ROLE_HEADQUARTERS_RECEPTIONIST_VALUE})
    WorkplaceBookingResponseDTO setParticipation(
            @PathVariable("workspaceId") Integer workspaceId,
            @PathVariable("workplaceId") Integer workplaceId,
            @PathVariable("bookingId") Integer bookingId
    );

    @DeleteMapping(
            path = "/workspaces/{workspaceId}/workplaces/{workplaceId}/bookings/{bookingId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void cancelBooking(
            @PathVariable("workspaceId") Integer workspaceId,
            @PathVariable("workplaceId") Integer workplaceId,
            @PathVariable("bookingId") Integer bookingId
    );

}
