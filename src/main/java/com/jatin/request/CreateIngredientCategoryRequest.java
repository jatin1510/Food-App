package com.jatin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIngredientCategoryRequest {
    private String name;
    private Long restaurantId;
}
