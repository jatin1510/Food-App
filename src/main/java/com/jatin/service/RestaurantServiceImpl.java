package com.jatin.service;

import com.jatin.dto.RestaurantDto;
import com.jatin.model.Address;
import com.jatin.model.Restaurant;
import com.jatin.model.User;
import com.jatin.repository.AddressRepository;
import com.jatin.repository.RestaurantRepository;
import com.jatin.repository.UserRepository;
import com.jatin.request.CreateRestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
        Address address = addressRepository.save(req.getAddress());

        Restaurant restaurant = new Restaurant();

        restaurant.setAddress(req.getAddress());
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationTime(LocalDateTime.now());
        restaurant.setOwner(user);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);

        if (updatedRestaurant.getCuisineType() != null) restaurant.setCuisineType(updatedRestaurant.getCuisineType());
        // if (updatedRestaurant.getAddress() != null) restaurant.setAddress(updatedRestaurant.getAddress());
        // if (updatedRestaurant.getContactInformation() != null) restaurant.setContactInformation(updatedRestaurant.getContactInformation());
        if (updatedRestaurant.getDescription() != null) restaurant.setDescription(updatedRestaurant.getDescription());
        // if (updatedRestaurant.getImages() != null) restaurant.setImages(updatedRestaurant.getImages());
        if (updatedRestaurant.getName() != null) restaurant.setName(updatedRestaurant.getName());
        // if (updatedRestaurant.getOpeningHours() != null) restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        restaurantRepository.delete(findRestaurantById(restaurantId));
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isEmpty()) {
            throw new Exception("Restaurant not found with id: " + id);
        }
        return optionalRestaurant.get();
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found with owner id: " + userId);
        }
        return restaurant;
    }

    @Override
    public RestaurantDto addToFavourites(Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setImages(restaurant.getImages());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setId(restaurantId);
        restaurantDto.setOpen(restaurant.isOpen());

        boolean isFavorite = false;
        List<RestaurantDto> favourites = user.getFavorites();
        for (RestaurantDto favourite : favourites) {
            if (favourite.getId().equals(restaurantId)) {
                isFavorite = true;
                break;
            }
        }

        if (isFavorite) {
            favourites.removeIf(object -> object.getId().equals(restaurantId));
        } else {
            favourites.add(restaurantDto);
        }

        userRepository.save(user);
        return restaurantDto;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
