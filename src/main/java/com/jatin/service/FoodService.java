package com.jatin.service;

import com.jatin.model.Category;
import com.jatin.model.Food;
import com.jatin.model.Restaurant;
import com.jatin.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

    public void deleteFood(Long foodId) throws Exception;

    // TODO: Parameters
    public List<Food> getRestaurantFoods(Long restaurantId,
                                        boolean isVegetarian,
                                        boolean isNonVegetarian,
                                        boolean isSeasonal,
                                        String foodCategory);

    public List<Food> searchFood(String keyword);

    public Food findFoodById(Long foodId) throws Exception;

    public Food updateAvailabilityStatus(Long foodId) throws Exception;
}
