package com.dp.spring.parallel.agora.database.entities;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class Event extends SoftDeletableAuditedEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "headquarters_id", nullable = false)
    private Headquarters headquarters;

    @Column(nullable = false)
    private String name;

    @Column(name = "on_date", nullable = false)
    private LocalDate onDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;


    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<EventBooking> bookings;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "headquartersId = " + headquarters.getId() + ", " +
                "name = " + name + ", " +
                "eventDate = " + onDate + ", " +
                "startTime = " + startTime + ", " +
                "endTime = " + endTime + ", " +
                "maxPlaces = " + maxPlaces +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(headquarters, event.headquarters) &&
                Objects.equals(name, event.name) &&
                Objects.equals(onDate, event.onDate) &&
                Objects.equals(startTime, event.startTime) &&
                Objects.equals(endTime, event.endTime) &&
                Objects.equals(maxPlaces, event.maxPlaces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headquarters, name, onDate, startTime, endTime, maxPlaces);
    }
}
