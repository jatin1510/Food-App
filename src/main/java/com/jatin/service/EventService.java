package com.jatin.service;

import com.jatin.model.Event;
import com.jatin.model.Restaurant;
import com.jatin.request.CreateEventRequest;

import java.util.List;

public interface EventService {
    public Event createEvent(CreateEventRequest req, Restaurant restaurant);

    public void deleteEvent(Long eventId) throws Exception;

    public List<Event> getRestaurantEvents(Long restaurantId);

    public Event findEventById(Long eventId) throws Exception;
}
