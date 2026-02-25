package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dim_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DimTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    private String category;

}
