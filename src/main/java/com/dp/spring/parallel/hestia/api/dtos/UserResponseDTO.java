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
        var builder = UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .address(user.getAddress())
                .role(user.getRole());
        return withRoleDetails(user, builder).build();
    }

    private static UserResponseDTOBuilder withRoleDetails(final User user, final UserResponseDTOBuilder builder) {
        return switch (user.getRole()) {
            case COMPANY_MANAGER -> builder.scopeId(((CompanyManagerUser) user).getCompany().getId())
                    .jobPosition(((CompanyManagerUser) user).getJobPosition());
            case HEADQUARTERS_RECEPTIONIST ->
                    builder.scopeId(((HeadquartersReceptionistUser) user).getHeadquarters().getId());
            case EMPLOYEE -> builder.scopeId(((EmployeeUser) user).getCompany().getId())
                    .jobPosition(((EmployeeUser) user).getJobPosition());
            default -> builder;
        };
    }
}