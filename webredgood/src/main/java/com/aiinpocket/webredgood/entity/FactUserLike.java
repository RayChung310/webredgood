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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private DimUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DimPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private DimTag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private DimLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_id")
    private DimDate date;

    @Column(name = "like_count")
    private Integer likeCount;


    public Long getUserId(){return user != null ? user.getId() : null;}
    public Long getPostId(){return post != null ? post.getId() : null;}
    public Long getTagId(){return tag != null ? tag.getId() : null;}
    public Long getLocationId(){return location != null ? location.getId() : null;}
    public Long getDateId(){return date != null ? date.getId() : null;}

}
