package com.jatin.controller;

import com.jatin.model.IngredientCategory;
import com.jatin.model.IngredientsItem;
import com.jatin.model.USER_ROLE;
import com.jatin.model.User;
import com.jatin.request.CreateIngredientCategoryRequest;
import com.jatin.request.CreateIngredientItemRequest;
import com.jatin.response.MessageResponse;
import com.jatin.service.IngredientsService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {
    @Autowired
    private IngredientsService ingredientsService;

    @Autowired
    private UserService userService;

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody CreateIngredientCategoryRequest req,
                                                                       @RequestHeader("Authorization") String jwt) throws Exception {
        System.out.println("createIngredientCategory Request: " + req);
        User user = userService.findUserByJwtToken(jwt);
        // TODO: Another restaurant owner cannot create ingredient category in my restaurant
        if (user.getRole() != USER_ROLE.ROLE_RESTAURANT_OWNER) {
            new ResponseEntity<>(new MessageResponse("You have not access"), HttpStatus.CREATED);
        }
        IngredientCategory category = ingredientsService.createIngredientCategory(req.getName(), req.getRestaurantId());
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PostMapping("/item")
    public ResponseEntity<IngredientsItem> createIngredientItem(@RequestBody CreateIngredientItemRequest req,
                                                                @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        // TODO: Another restaurant owner cannot create ingredient in my restaurant
        if (user.getRole() != USER_ROLE.ROLE_RESTAURANT_OWNER) {
            new ResponseEntity<>(new MessageResponse("You have not access"), HttpStatus.CREATED);
        }
        IngredientsItem item = ingredientsService.createIngredientItem(req.getRestaurantId(), req.getName(), req.getCategoryId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/item/{ingredientId}/stock")
    public ResponseEntity<IngredientsItem> updateIngredientStock(@PathVariable Long ingredientId,
                                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        // TODO: Another restaurant owner cannot update ingredient stock in my restaurant
        if (user.getRole() != USER_ROLE.ROLE_RESTAURANT_OWNER) {
            new ResponseEntity<>(new MessageResponse("You have not access"), HttpStatus.CREATED);
        }

        IngredientsItem updatedItem = ingredientsService.updateStock(ingredientId);
        return new ResponseEntity<>(updatedItem, HttpStatus.ACCEPTED);
    }

    @GetMapping("/restaurant/{restaurantId}/items")
    public ResponseEntity<List<IngredientsItem>> getRestaurantIngredientItems(@PathVariable Long restaurantId,
                                                                              @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        // TODO: Another restaurant owner cannot see my ingredients

        List<IngredientsItem> items = ingredientsService.findRestaurantIngredients(restaurantId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}/categories")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategories(@PathVariable Long restaurantId,
                                                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        // TODO: Another restaurant owner cannot see my ingredients

        List<IngredientCategory> items = ingredientsService.findIngredientCategoryByRestaurantId(restaurantId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}