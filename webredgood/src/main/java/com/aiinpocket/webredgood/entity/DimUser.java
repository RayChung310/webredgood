package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "dim_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DimUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String email;
    private String city;
    private String county;

    @Column(name = "location_id")
    private Long  locationId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreateAt() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
