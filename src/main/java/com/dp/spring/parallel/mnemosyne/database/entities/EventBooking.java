package com.dp.spring.parallel.mnemosyne.database.entities;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hestia.database.entities.User;
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
@Table(uniqueConstraints = {
        // Worker can't book for the same event more than once.
        @UniqueConstraint(columnNames = {"worker_id", "event_id", "is_active"}),
})
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class EventBooking extends SoftDeletableAuditedEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "workerId = " + worker.getId() +
                ")";
    }
}
