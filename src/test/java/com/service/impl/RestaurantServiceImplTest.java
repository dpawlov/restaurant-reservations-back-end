package com.service.impl;

import com.domain.Restaurant;
import com.repository.RestaurantRepository;
import com.service.dto.RestaurantDto;
import com.service.mapper.RestaurantCreateMapper;
import com.service.mapper.RestaurantCreateMapperImpl;
import com.service.mapper.RestaurantMapper;
import com.service.mapper.RestaurantMapperImpl;
import com.service.specs.RestaurantSpecificationService;
import com.web.errors.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    private RestaurantServiceImpl restaurantService;
    private final RestaurantMapper restaurantMapper = new RestaurantMapperImpl();
    private final RestaurantCreateMapper restaurantCreateMapper = new RestaurantCreateMapperImpl();
    private RestaurantSpecificationService restaurantSpecificationService;
    private Restaurant restaurant;

    private static final String DEFAULT_RESTAURANT_NAME = "Test name";
    private static final float DEFAULT_RESTAURANT_RATING = 4.4f;
    private static final String DEFAULT_RESTAURANT_DESCRIPTION = "Test description";

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();

        restaurant.setId(1L);
        restaurant.setName(DEFAULT_RESTAURANT_NAME);
        restaurant.setRating(DEFAULT_RESTAURANT_RATING);
        restaurant.setDescription(DEFAULT_RESTAURANT_DESCRIPTION);

        restaurantService = new RestaurantServiceImpl(restaurantRepository, restaurantMapper, restaurantCreateMapper,
                restaurantSpecificationService);
    }

    @Test
    void testCreateRestaurantMethod() {
        //when
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantDto expectedReservation = restaurantService.create(restaurantCreateMapper.toDto(restaurant));

        //then
        assertThat(restaurant.getId()).isEqualTo(expectedReservation.getId());
    }

    @Test
    void findById() {
        //when
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        RestaurantDto expectedRestaurant = restaurantService.findById(restaurant.getId());

        //then
        assertThat(expectedRestaurant).isNotNull();
    }

    @Test()
    void findById_withInvalidId_expectNotFoundException() {
        //given
        restaurant.setId(null);

        //when
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(NotFoundException.class, () -> restaurantService.findById(restaurant.getId()));
        assertEquals("Restaurant with the following id does not exists in the database: " + restaurant.getId(), exception.getMessage());
    }

    @Test()
    void update_withInvalidId_expectNotFoundException() {
        //given
        restaurant.setId(null);

        //when
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(NotFoundException.class, () -> restaurantService.updateRestaurant(restaurantMapper.toDto(restaurant)));
        assertEquals("Restaurant with the following id does not exists in the database: " + restaurant.getId(), exception.getMessage());
    }

    @Test()
    void delete_withInvalidId_expectNotFoundException() {
        //given
        restaurant.setId(null);

        //then
        Exception exception = assertThrows(NotFoundException.class, () -> restaurantService.delete(restaurant.getId()));
        assertEquals("Restaurant with the following id does not exists in the database: " + restaurant.getId(), exception.getMessage());
    }

    @Test
    void updateRestaurant() {
        //when
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        restaurant.setName("New Restaurant");
        restaurant.setRating(3.3f);

        RestaurantDto expectedRestaurant = restaurantService.updateRestaurant(restaurantMapper.toDto(restaurant));

        //then
        assertThat(expectedRestaurant.getName()).isEqualTo("New Restaurant");
        assertThat(expectedRestaurant.getRating()).isEqualTo(3.3f);
    }

    @Test
    void delete() {
        //when
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        restaurantService.delete(restaurant.getId());

        //then
        verify(restaurantRepository, times(1)).deleteById(restaurant.getId());
    }
}