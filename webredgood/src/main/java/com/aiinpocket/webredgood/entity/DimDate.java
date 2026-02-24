package com.aiinpocket.webredgood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dim_date")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DimDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long id;

    @Column(name = "full_date")
    private java.time.LocalDate fullDate;

    private Integer year;
    private Integer month;
    private Integer day;

    @Column(name = "day_of_week", length = 10)
    private String dayOfWeek;

    private Integer quarter;

}
