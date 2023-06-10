package com.dp.spring.parallel.hephaestus.database.entities;

import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "workspace_id", "is_active"}))
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class Workplace extends SoftDeletableAuditedEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkplaceType type;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "workspaceId = " + workspace.getId() + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "type = " + type +
                ")";
    }
}
