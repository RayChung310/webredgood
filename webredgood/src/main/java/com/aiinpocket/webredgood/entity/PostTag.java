package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PostTagId.class)
public class PostTag {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;

}
