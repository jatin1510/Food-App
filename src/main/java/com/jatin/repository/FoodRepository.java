package com.jatin.repository;

import com.jatin.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByRestaurantId(Long restaurantId);

    @Query("SELECT f from Food f WHERE f.name LIKE concat('%', :keyword, '%') or f.foodCategory.name LIKE concat('%', :keyword, '%')")
    List<Food> searchFood(@Param("keyword") String keyword);
}
