package com.jatin.request;

import com.jatin.model.Category;
import com.jatin.model.IngredientsItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateFoodRequest {
    private String name;

    private String description;

    private Long price;

    private Category foodCategory;

    private List<String> images;

    private Long restaurantId;

    private boolean isVegetarian;

    private boolean isSeasonal;

    List<IngredientsItem> ingredients;
}
