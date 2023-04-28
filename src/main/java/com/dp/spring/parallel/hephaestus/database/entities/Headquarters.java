package com.dp.spring.parallel.hephaestus.database.entities;

import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.springcore.database.annotations.ActiveEntities;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
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
public class Headquarters extends SoftDeletableAuditedEntity<Integer> {
    private String name;

    @ManyToOne
    @JoinColumn(name = "headquarters_id")
    private Company company;

    @OneToMany(mappedBy = "headquarters", fetch = FetchType.LAZY)
    private Set<HeadquartersReceptionistUser> receptionists;
}
