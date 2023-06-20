package com.dp.spring.parallel.hephaestus.database.entities;

import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.util.Objects;
import java.util.Set;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "headquarters_id", "is_active", "last_modified_date"}))
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class Workspace extends SoftDeletableAuditedEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "headquarters_id", nullable = false)
    private Headquarters headquarters;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkspaceType type;

    private String floor;


    @OneToMany(mappedBy = "workspace", fetch = FetchType.LAZY)
    private Set<Workplace> workplaces;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "headquartersId = " + headquarters.getId() + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "type = " + type + ", " +
                "floor = " + floor +
                ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workspace workspace = (Workspace) o;
        return Objects.equals(id, workspace.id) &&
                Objects.equals(headquarters, workspace.headquarters) &&
                Objects.equals(name, workspace.name) &&
                Objects.equals(description, workspace.description) &&
                type == workspace.type &&
                Objects.equals(floor, workspace.floor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headquarters, name, description, type, floor);
    }
}
