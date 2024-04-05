package com.jatin.controller;

import com.jatin.model.Food;
import com.jatin.model.User;
import com.jatin.service.FoodService;
import com.jatin.service.RestaurantService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/food/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String keyword) throws Exception {
        return new ResponseEntity<>(foodService.searchFood(keyword), HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}/foods")
    public ResponseEntity<List<Food>> getRestaurantFoods(@PathVariable Long restaurantId,
                                                         @RequestParam(required = false) boolean vegetarian,
                                                         @RequestParam(required = false) boolean nonVegetarian,
                                                         @RequestParam(required = false) boolean seasonal,
                                                         @RequestParam(required = false) String foodCategory
                                                         ) throws Exception {
        List<Food> foods = foodService.getRestaurantFoods(restaurantId, vegetarian, nonVegetarian, seasonal, foodCategory);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
