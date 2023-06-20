package com.dp.spring.parallel.mnemosyne.database.entities;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.util.Objects;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(uniqueConstraints = {
        // Worker can't book for the same event more than once.
        @UniqueConstraint(columnNames = {"worker_id", "event_id", "is_active", "last_modified_date"}),
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
                "workerId = " + worker.getId() + ", " +
                "eventId = " + event.getId() +
                ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventBooking booking = (EventBooking) o;
        return Objects.equals(id, booking.getId()) &&
                Objects.equals(worker.getId(), booking.worker.getId()) &&
                Objects.equals(event.getId(), booking.event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, worker.getId(), event.getId());
    }
}
