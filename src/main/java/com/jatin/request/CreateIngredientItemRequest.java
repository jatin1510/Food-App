package com.jatin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIngredientItemRequest {
    private String name;
    private Long categoryId;
    private Long restaurantId;
}
