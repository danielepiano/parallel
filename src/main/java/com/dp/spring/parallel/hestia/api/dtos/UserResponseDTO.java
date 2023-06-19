package com.dp.spring.parallel.hestia.api.dtos;

import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {

    Integer id;
    String email;
    String firstName;
    String lastName;

    LocalDate birthDate;
    String phoneNumber;
    String city;
    String address;

    UserRole role;

    Integer scopeId;
    String jobPosition;


    public static UserResponseDTO of(final User user) {
        return switch (user.getRole()) {
            case COMPANY_MANAGER -> of((CompanyManagerUser) user);
            case HEADQUARTERS_RECEPTIONIST -> of((HeadquartersReceptionistUser) user);
            case EMPLOYEE -> of((EmployeeUser) user);
            default -> base(user);
        };
    }

    public static UserResponseDTO base(final User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }

    public static UserResponseDTO of(final CompanyManagerUser user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .address(user.getAddress())
                .role(user.getRole())
                .scopeId(user.getCompany().getId())
                .jobPosition(user.getJobPosition())
                .build();
    }

    public static UserResponseDTO of(final HeadquartersReceptionistUser user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .address(user.getAddress())
                .role(user.getRole())
                .scopeId(user.getHeadquarters().getId())
                .build();
    }

    public static UserResponseDTO of(final EmployeeUser user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .address(user.getAddress())
                .role(user.getRole())
                .scopeId(user.getCompany().getId())
                .jobPosition(user.getJobPosition())
                .build();
    }
}