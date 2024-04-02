package com.jatin.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateEventRequest {
    private List<String> images;

    private String name;

    private String description;

    private String location;

    private String startDateAndTime;

    private String endDateAndTime;

    private Long restaurantId;
}