package com.jatin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private List<String> images;

    private String name;

    private String description;

    private String location;

    private String startDateAndTime;

    private String endDateAndTime;

    @ManyToOne
    private Restaurant restaurant;
}
