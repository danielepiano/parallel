package com.dp.spring.parallel.hestia.database.entities;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.dp.spring.springcore.database.entities.SoftDeletableAuditedEntity.SOFT_DELETE_CLAUSE;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = SOFT_DELETE_CLAUSE)
@Inheritance
@DiscriminatorColumn(name = "role")
@Table(name = "user_details")
public class User extends SoftDeletableAuditedEntity<Integer> implements UserDetails {
    // #--- PERSONAL DETAILS ---------------------------------------------------------
    @Column(nullable = false)
    protected String firstName;

    @Column(nullable = false)
    protected String lastName;

    @Column(nullable = false)
    protected LocalDate birthDate;

    // # --- CONTACTS ----------------------------------------------------------------
    @Column(nullable = false)
    protected String phoneNumber;

    @Column(nullable = false)
    protected String city;

    @Column(nullable = false)
    protected String address;

    // #--- ACCOUNT DETAILS ----------------------------------------------------------
    // profilePicture ?
    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected UserRole role;


    @ManyToMany(mappedBy = "observers", fetch = FetchType.EAGER)
    private List<Headquarters> favoriteHeadquarters;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getGrantedAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.active;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "birthDate = " + birthDate + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "city = " + city + ", " +
                "address = " + address + ", " +
                "email = " + email + ", " +
                "password = " + password + ", " +
                "role = " + role + ", " +
                "createdBy = " + createdBy + ", " +
                "createdDate = " + createdDate + ", " +
                "lastModifiedBy = " + lastModifiedBy + ", " +
                "lastModifiedDate = " + lastModifiedDate + ", " +
                "active = " + active +
                ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(birthDate, user.birthDate) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(city, user.city) &&
                Objects.equals(address, user.address) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate, phoneNumber, city, address, email, password, role);
    }
}
