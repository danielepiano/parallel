package com.dp.spring.parallel.hestia.database.entities;

import com.dp.spring.parallel.hestia.database.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(UserRole.Constants.ADMIN_VALUE)
public class AdminUser extends User {
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "birthDate = " + birthDate + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "city = " + city + ", " +
                "address = " + address + ", " +
                "email = " + email + ", " +
                "password = " + password + ", " +
                "role = " + role + ", " +
                "createdBy = " + createdBy + ", " +
                "createdDate = " + createdDate + ", " +
                "lastModifiedBy = " + lastModifiedBy + ", " +
                "lastModifiedDate = " + lastModifiedDate + ", " +
                "active = " + active +
                ")";
    }
}