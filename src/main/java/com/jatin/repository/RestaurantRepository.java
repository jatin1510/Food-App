package com.jatin.repository;

import com.jatin.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE lower(r.name) like lower(concat('%', :query, '%')) or lower(r.cuisineType) like lower(concat('%', :query, '%'))")
    public List<Restaurant> findBySearchQuery(@Param("query") String query);

    public Restaurant findByOwnerId(Long userId);
}
