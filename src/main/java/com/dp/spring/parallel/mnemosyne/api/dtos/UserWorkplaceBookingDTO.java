package com.dp.spring.parallel.mnemosyne.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;
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
public class UserWorkplaceBookingDTO {

    CompanyInfoDTO company;
    HeadquartersInfoDTO headquarters;
    WorkspaceInfoDTO workspace;
    WorkplaceInfoDTO workplace;

    LocalDate bookingDate;
    LocalDate bookedOn;


    public static UserWorkplaceBookingDTO of(final WorkplaceBooking booking) {
        return UserWorkplaceBookingDTO.builder()
                .workplace(WorkplaceInfoDTO.of(booking.getWorkplace()))
                .workspace(WorkspaceInfoDTO.of(booking.getWorkplace().getWorkspace()))
                .headquarters(HeadquartersInfoDTO.of(booking.getWorkplace().getWorkspace().getHeadquarters()))
                .company(CompanyInfoDTO.of(booking.getWorkplace().getWorkspace().getHeadquarters().getCompany()))
                .bookingDate(booking.getBookingDate())
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