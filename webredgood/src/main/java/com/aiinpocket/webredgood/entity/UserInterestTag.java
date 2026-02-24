package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_interest_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserInterestTagId.class)
public class UserInterestTag {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "weight", columnDefinition = "numeric")
    private Double weight;

    @Column(name = "last_updated")
    private Instant lastUpdated;

}
