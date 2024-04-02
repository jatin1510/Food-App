package com.jatin.service;

import com.jatin.model.Event;
import com.jatin.model.Restaurant;
import com.jatin.repository.EventRepository;
import com.jatin.request.CreateEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event createEvent(CreateEventRequest req, Restaurant restaurant) {
        Event event = new Event();

        event.setImages(req.getImages());
        event.setName(req.getName());
        event.setRestaurant(restaurant);
        event.setLocation(req.getLocation());
        event.setStartDateAndTime(req.getStartDateAndTime());
        event.setEndDateAndTime(req.getEndDateAndTime());
        event.setDescription(req.getDescription());

        Event savedEvent = eventRepository.save(event);
        restaurant.getEvents().add(savedEvent);

        return savedEvent;
    }

    @Override
    public void deleteEvent(Long eventId) throws Exception {
        Event event = findEventById(eventId);
        event.setRestaurant(null);
        eventRepository.delete(event);
    }

    @Override
    public List<Event> getRestaurantEvents(Long restaurantId) {
        return eventRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Event findEventById(Long eventId) throws Exception {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            throw new Exception("Event does not exist");
        }
        return optionalEvent.get();
    }
}