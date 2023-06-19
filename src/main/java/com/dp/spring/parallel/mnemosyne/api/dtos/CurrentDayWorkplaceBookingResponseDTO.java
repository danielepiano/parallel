package com.dp.spring.parallel.mnemosyne.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.entities.User;
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
public class CurrentDayWorkplaceBookingResponseDTO {

    Integer id;

    UserInfoDTO worker;
    WorkspaceInfoDTO workspace;
    WorkplaceInfoDTO workplace;

    LocalDate bookingDate;
    LocalDate bookedOn;
    boolean present;


    public static CurrentDayWorkplaceBookingResponseDTO of(final WorkplaceBooking booking) {
        return CurrentDayWorkplaceBookingResponseDTO.builder()
                .id(booking.getId())
                .worker((UserInfoDTO.of(booking.getWorker())))
                .workplace(WorkplaceInfoDTO.of(booking.getWorkplace()))
                .workspace(WorkspaceInfoDTO.of(booking.getWorkplace().getWorkspace()))
                .bookingDate(booking.getBookingDate())
                .bookedOn(Instant.ofEpochMilli(booking.getCreatedDate()).atZone(ZoneId.systemDefault()).toLocalDate())
                .present(booking.isPresent())
                .build();
    }

    @Jacksonized
    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserInfoDTO {
        Integer id;

        String firstName;
        String lastName;
        String email;
        String companyName;

        static UserInfoDTO of(final User worker) {
            final String companyName = switch (worker.getRole()) {
                case COMPANY_MANAGER -> ((CompanyManagerUser) worker).getCompany().getName();
                case EMPLOYEE -> ((EmployeeUser) worker).getCompany().getName();
                default -> "";
            };

            return UserInfoDTO.builder()
                    .id(worker.getId())
                    .firstName(worker.getFirstName())
                    .lastName(worker.getLastName())
                    .email(worker.getEmail())
                    .companyName(companyName)
                    .build();
        }
    }

    @Jacksonized
    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WorkspaceInfoDTO {
        Integer id;
        String name;
        String floor;

        static WorkspaceInfoDTO of(final Workspace workspace) {
            return WorkspaceInfoDTO.builder()
                    .id(workspace.getId())
                    .name(workspace.getName())
                    .floor(workspace.getFloor())
                    .build();
        }
    }

    @Jacksonized
    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WorkplaceInfoDTO {
        Integer id;
        String name;

        static WorkplaceInfoDTO of(final Workplace workplace) {
            return WorkplaceInfoDTO.builder()
                    .id(workplace.getId())
                    .name(workplace.getName())
                    .build();
        }
    }
}