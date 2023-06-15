package com.dp.spring.parallel.agora.database.entities;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class Event extends SoftDeletableAuditedEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "headquarters_id", nullable = false)
    private Headquarters headquarters;

    @Column(nullable = false)
    private String name;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;


    /* @todo serve?
    @OneToMany(mappedBy = "workspace", fetch = FetchType.LAZY)
    private Set<EventBooking> bookings;*/


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "headquartersId = " + headquarters.getId() + ", " +
                "name = " + name + ", " +
                "eventDate = " + eventDate + ", " +
                "startTime = " + startTime + ", " +
                "endTime = " + endTime + ", " +
                "maxPlaces = " + maxPlaces +
                ")";
    }
}
