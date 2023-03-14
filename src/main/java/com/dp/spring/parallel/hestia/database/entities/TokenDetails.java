package com.dp.spring.parallel.hestia.database.entities;

import com.dp.spring.parallel.hestia.database.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class TokenDetails {
    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true)
    public String token;

    @ColumnDefault("false")
    private boolean revoked;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
