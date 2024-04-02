package com.jatin.controller;

import com.jatin.model.Event;
import com.jatin.model.Restaurant;
import com.jatin.model.User;
import com.jatin.repository.EventRepository;
import com.jatin.request.CreateEventRequest;
import com.jatin.response.MessageResponse;
import com.jatin.service.EventService;
import com.jatin.service.RestaurantService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody CreateEventRequest req,
                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Event event = eventService.createEvent(req, restaurant);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Event> events = eventRepository.findAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Event>> getRestaurantEvents(@PathVariable Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        List<Event> events = eventRepository.findByRestaurantId(restaurant.getId());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long eventId,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Event event = eventService.findEventById(eventId);
        if (event.getRestaurant().getOwner() != user) {
            return new ResponseEntity<>(new MessageResponse("You have not access"), HttpStatus.BAD_REQUEST);
        }

        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(new MessageResponse("Event deleted successfully"), HttpStatus.ACCEPTED);
    }
}