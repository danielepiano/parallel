package com.dp.spring.parallel.hephaestus.database.entities;

import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.util.Set;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "city", "address", "is_active"}))
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class Headquarters extends SoftDeletableAuditedEntity<Integer> {
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String feDescription;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "headquarters", fetch = FetchType.LAZY)
    private Set<HeadquartersReceptionistUser> receptionists;

    @OneToMany(mappedBy = "headquarters", fetch = FetchType.LAZY)
    private Set<Workspace> workspaces;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "city = " + city + ", " +
                "address = " + address + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "feDescription = " + feDescription + ", " +
                "companyId = " + company.getId() +
                ")";
    }
}
