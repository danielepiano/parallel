package com.dp.spring.parallel.talos.api.dtos;

import com.dp.spring.parallel.hestia.database.entities.*;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UserToken {
    @NotNull Integer id;
    @NotNull String email;
    @NotNull String firstName;
    @NotNull String lastName;
    @NotNull UserRole role;
    Integer scopeId;
    String jobPosition;


    public static UserToken of(final User user) {
        return UserToken.buildUserToken(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                null,
                null
        );
    }

    public static UserToken of(final AdminUser user) {
        return UserToken.of((User) user);
    }

    public static UserToken of(final CompanyManagerUser user) {
        return UserToken.buildUserToken(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCompany().getId(),
                user.getJobPosition()
        );
    }

    public static UserToken of(final HeadquartersReceptionistUser user) {
        return UserToken.buildUserToken(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getHeadquarters().getId(),
                null
        );
    }

    public static UserToken of(final EmployeeUser user) {
        return UserToken.buildUserToken(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCompany().getId(),
                user.getJobPosition()
        );
    }

    public static UserToken buildUserToken(Integer id, String firstName, String lastName, UserRole role, Integer scopeId, String jobPosition) {
        return UserToken.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .scopeId(scopeId)
                .jobPosition(jobPosition)
                .build();
    }
}