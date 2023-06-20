package com.dp.spring.parallel.talos.database.entities;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.talos.database.enums.TokenType;
import com.dp.spring.springcore.database.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class TokenDetails extends BaseEntity<Integer> {
    @Column(nullable = false, unique = true, columnDefinition = "text")
    private String token;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean revoked = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenDetails that = (TokenDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(revoked, that.revoked) &&
                Objects.equals(token, that.token) &&
                Objects.equals(tokenType, that.tokenType) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, revoked, tokenType, user);
    }
}
