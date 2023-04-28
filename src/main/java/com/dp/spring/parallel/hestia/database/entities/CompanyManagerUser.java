package com.dp.spring.parallel.hestia.database.entities;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
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
@DiscriminatorValue(UserRole.Constants.COMPANY_MANAGER_VALUE)
public class CompanyManagerUser extends User {
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String jobPosition;
}