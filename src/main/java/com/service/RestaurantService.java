package com.service;

import com.service.criteria.RestaurantCriteria;
import com.service.dto.RestaurantDto;
import com.service.dto.RestaurantCreateDto;

import java.util.List;

public interface RestaurantService {

    /**
     * Creates a Restaurant.
     *
     * @param restaurantCreateDto the restaurant details which will be created and saved.
     * @return RestaurantGetDto entity.
     */
    RestaurantDto create(RestaurantCreateDto restaurantCreateDto);

    /**
     * Return all Restaurants in the Database.
     * @return List of Restaurants.
     */
    List<RestaurantDto> findAll(RestaurantCriteria restaurantCriteria);

    /**
     * Updates a Restaurant.
     *
     * @param restaurantDto restaurant details which we use to update the restaurant.
     * @return Restaurant entity.
     */
    RestaurantDto updateRestaurant(RestaurantDto restaurantDto);

    /**
     * Deletes a Restaurant.
     *
     * @param restaurantId the restaurant which will be deleted.
     */
    void delete(Long restaurantId);

    /**
     * Finds a Restaurant by its id.
     *
     * @param restaurantId the restaurant which will be returned.
     * @return Restaurant entity.
     */
    RestaurantDto findById(Long restaurantId);
}
