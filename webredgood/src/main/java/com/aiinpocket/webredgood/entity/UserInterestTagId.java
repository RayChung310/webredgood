package com.aiinpocket.webredgood.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserInterestTagId implements Serializable {

    private Long userId;
    private Long tagId;
}
