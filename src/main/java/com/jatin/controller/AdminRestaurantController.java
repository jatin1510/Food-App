package com.jatin.controller;

import com.jatin.model.Restaurant;
import com.jatin.model.User;
import com.jatin.request.CreateRestaurantRequest;
import com.jatin.response.MessageResponse;
import com.jatin.service.RestaurantService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody CreateRestaurantRequest req,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.createRestaurant(req, user);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? super Restaurant> updateRestaurant(@RequestBody CreateRestaurantRequest req,
                                                       @RequestHeader("Authorization") String jwt,
                                                       @PathVariable Long id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        // TODO: The current user must be owner
        Restaurant toBeUpdated = restaurantService.findRestaurantById(id);
        if (toBeUpdated.getOwner() != user) {
            return new ResponseEntity<>("You have not access", HttpStatus.BAD_REQUEST);
        }

        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, req);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? super MessageResponse> deleteRestaurant(@RequestHeader("Authorization") String jwt,
                                                            @PathVariable Long id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        // TODO: The current user must be owner
        Restaurant toBeDeleted = restaurantService.findRestaurantById(id);
        if (toBeDeleted.getOwner() != user) {
            return new ResponseEntity<>("You have not access", HttpStatus.BAD_REQUEST);
        }

        restaurantService.deleteRestaurant(id);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Restaurant Deleted Successfully");
        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<? super Restaurant> updateRestaurantStatus(@RequestHeader("Authorization") String jwt,
                                                             @PathVariable Long id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        // TODO: The current user must be owner
        Restaurant toBeUpdated = restaurantService.findRestaurantById(id);
        if (toBeUpdated.getOwner() != user) {
            return new ResponseEntity<>("You have not access", HttpStatus.BAD_REQUEST);
        }

        Restaurant updateRestaurant = restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(updateRestaurant, HttpStatus.ACCEPTED);
    }

    @GetMapping("/mine")
    public ResponseEntity<Restaurant> findRestaurantByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
