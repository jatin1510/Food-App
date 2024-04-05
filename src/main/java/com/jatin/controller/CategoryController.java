package com.jatin.controller;

import com.jatin.model.Category;
import com.jatin.model.Restaurant;
import com.jatin.model.User;
import com.jatin.request.CategoryRequest;
import com.jatin.service.CategoryService;
import com.jatin.service.RestaurantService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Category createdCategory = categoryService.createCategory(categoryRequest.getName(), user.getId());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/admin/category/all")
    public ResponseEntity<List<Category>> getRestaurantCategoriesForAdmin(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        List<Category> categories = categoryService.findCategoryByRestaurantId(restaurant.getId());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/category/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getRestaurantCategoriesForCustomer(@PathVariable Long restaurantId) throws Exception {
        List<Category> categories = categoryService.findCategoryByRestaurantId(restaurantId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
