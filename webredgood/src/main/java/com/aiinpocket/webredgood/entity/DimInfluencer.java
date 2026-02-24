package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dim_influencer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DimInfluencer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "influencer_id")
    private Long  id;

    private String name;
    private String platform;

    @Column(name = "follower_count")
    private Integer followerCount;

}
