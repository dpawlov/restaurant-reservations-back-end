package com.web;

import com.service.RestaurantService;
import com.service.criteria.RestaurantCriteria;
import com.service.dto.RestaurantDto;
import com.service.dto.RestaurantCreateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/restaurant")
    public RestaurantDto createRestaurant(@RequestBody @Valid RestaurantCreateDto restaurantCreateDto) {

        LOGGER.info("Rest request for creating Restaurant: " + restaurantCreateDto.toString());

        return restaurantService.create(restaurantCreateDto);
    }

    @GetMapping("/restaurant")
    public List<RestaurantDto> getAllRestaurants(RestaurantCriteria restaurantCriteria) {
        LOGGER.info("Rest request for getting all Restaurants");

        return restaurantService.findAll(restaurantCriteria);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public RestaurantDto getRestaurantById(@PathVariable Long restaurantId) {

        LOGGER.info("Rest request for getting Restaurant by id: " + restaurantId);

        return restaurantService.findById(restaurantId);
    }

    @PutMapping("/restaurant")
    public RestaurantDto updateRestaurant(@RequestBody @Valid RestaurantDto restaurantDto) {

        LOGGER.info("Rest request for updating Restaurant: " + restaurantDto.toString());

        return restaurantService.updateRestaurant(restaurantDto);
    }

    @DeleteMapping("/restaurant/{restaurantId}")
    public void deleteRestaurant(@PathVariable Long restaurantId) {

        LOGGER.info("Rest request for deleting Restaurant by id: " + restaurantId);

        restaurantService.delete(restaurantId);
    }
}
