package com.jatin.controller;

import com.jatin.model.Food;
import com.jatin.model.Restaurant;
import com.jatin.model.User;
import com.jatin.request.CreateFoodRequest;
import com.jatin.response.MessageResponse;
import com.jatin.service.FoodService;
import com.jatin.service.RestaurantService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Food food = foodService.createFood(req, req.getFoodCategory(), restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.findFoodById(id);
        if (food.getRestaurant().getOwner() != user) {
            return new ResponseEntity<>(new MessageResponse("You have not access"), HttpStatus.BAD_REQUEST);
        }

        foodService.deleteFood(id);
        return new ResponseEntity<>(new MessageResponse("Food deleted successfully"), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<? super Food> updateFoodAvailabilityStatus(@PathVariable Long id,
                                                                        @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.findFoodById(id);
        if (food.getRestaurant().getOwner() != user) {
            return new ResponseEntity<>(new MessageResponse("You have not access"), HttpStatus.BAD_REQUEST);
        }

        Food returnFood = foodService.updateAvailabilityStatus(id);
        return new ResponseEntity<>(returnFood, HttpStatus.ACCEPTED);
    }
}
