package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dim_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DimLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "county_name")
    private String countyName;

    private String region;

    private BigDecimal latitude;
    private BigDecimal longitude;

}
