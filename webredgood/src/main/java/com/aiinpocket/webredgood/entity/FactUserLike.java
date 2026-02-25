package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fact_user_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactUserLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "date_id")
    private Long dateId;

    @Column(name = "like_count")
    private Integer likeCount;
}
