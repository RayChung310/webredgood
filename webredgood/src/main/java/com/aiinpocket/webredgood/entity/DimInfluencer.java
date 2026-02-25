package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dim_influencer")
@Data
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
