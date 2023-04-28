package com.dp.spring.parallel.hestia.database.entities;

import com.dp.spring.parallel.hestia.database.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(UserRole.Constants.ADMIN_VALUE)
public class AdminUser extends User {
}