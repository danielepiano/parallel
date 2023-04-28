package com.dp.spring.parallel.hephaestus.database.entities;

import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.springcore.database.annotations.ActiveEntities;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@ActiveEntities
public class Company extends SoftDeletableAuditedEntity<Integer> {
    private String name;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<Headquarters> headquarters;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<EmployeeUser> employees;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<CompanyManagerUser> managers;
}
