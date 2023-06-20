package com.dp.spring.parallel.hephaestus.database.entities;

import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "city", "address", "is_active", "last_modified_date"}))
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
public class Company extends SoftDeletableAuditedEntity<Integer> {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String description;

    private String websiteUrl;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<Headquarters> headquarters;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<EmployeeUser> employees;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<CompanyManagerUser> managers;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "city = " + city + ", " +
                "address = " + address + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "description = " + description +
                ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) &&
                Objects.equals(name, company.name) &&
                Objects.equals(city, company.city) &&
                Objects.equals(address, company.address) &&
                Objects.equals(phoneNumber, company.phoneNumber) &&
                Objects.equals(description, company.description) &&
                Objects.equals(websiteUrl, company.websiteUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, address, phoneNumber, description, websiteUrl);
    }
}
