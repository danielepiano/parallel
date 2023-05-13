package com.dp.spring.parallel.talos.database.entities;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.talos.database.enums.TokenType;
import com.dp.spring.springcore.database.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
}
