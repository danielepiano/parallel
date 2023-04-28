package com.dp.spring.parallel.hestia.database.entities;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(UserRole.Constants.HEADQUARTERS_RECEPTIONIST_VALUE)
public class HeadquartersReceptionistUser extends User {
    @ManyToOne
    @JoinColumn(name = "headquarters_id")
    private Headquarters headquarters;
}