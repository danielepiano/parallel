package com.dp.spring.parallel.ponos.database.entities;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Table(uniqueConstraints = {
        // Worker can't book more than one workplace for the same date.
        @UniqueConstraint(columnNames = {"worker_id", "booking_date", "is_active"}),
        // Workplace can't be booked more than once on a specific date.
        @UniqueConstraint(columnNames = {"workplace_id", "booking_date", "is_active"})
})
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class WorkplaceBooking extends SoftDeletableAuditedEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    @ManyToOne
    @JoinColumn(name = "workplace_id", nullable = false)
    private Workplace workplace;

    @Column(name = "booking_date", nullable = false)
    protected LocalDate bookingDate;

    @Column(name = "is_present", nullable = false, columnDefinition = "boolean default false")
    protected boolean present;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "workerId = " + worker.getId() + ", " +
                "workplaceId = " + workplace.getId() + ", " +
                "bookingDate = " + bookingDate + ", " +
                "present = " + present +
                ")";
    }
}
