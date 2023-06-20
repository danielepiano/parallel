package com.dp.spring.parallel.hephaestus.database.entities;

import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import com.dp.spring.springcore.observer.SimplePublisher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "city", "address", "is_active", "last_modified_date"}))
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class Headquarters extends SoftDeletableAuditedEntity<Integer> implements SimplePublisher<User> {
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    @OneToMany(mappedBy = "headquarters", fetch = FetchType.LAZY)
    private Set<HeadquartersReceptionistUser> receptionists;

    @OneToMany(mappedBy = "headquarters", fetch = FetchType.LAZY)
    private Set<Workspace> workspaces;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "worker_favorite_headquarters",
            joinColumns = @JoinColumn(name = "headquarters_id"),
            inverseJoinColumns = @JoinColumn(name = "worker_id")
    )
    private List<User> observers;


    @Override
    public List<User> getObservers() {
        return observers;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "city = " + city + ", " +
                "address = " + address + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "description = " + description + ", " +
                "companyId = " + company.getId() +
                ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Headquarters that = (Headquarters) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(city, that.city) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(description, that.description) &&
                Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, address, phoneNumber, description, company);
    }
}
