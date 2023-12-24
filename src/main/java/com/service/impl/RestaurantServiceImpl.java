package com.service.impl;

import com.repository.RestaurantRepository;
import com.service.RestaurantService;
import com.service.criteria.RestaurantCriteria;
import com.service.dto.RestaurantDto;
import com.service.dto.RestaurantCreateDto;
import com.service.mapper.RestaurantCreateMapper;
import com.service.mapper.RestaurantMapper;
import com.service.specs.RestaurantSpecificationService;
import com.web.errors.NotFoundException;
import com.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantServiceImpl.class);
    private static final String EXCEPTION = "Restaurant with the following id does not exists in the database: ";

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantCreateMapper restaurantCreateMapper;
    private final RestaurantSpecificationService restaurantSpecificationService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper,
            RestaurantCreateMapper restaurantCreateMapper,
            RestaurantSpecificationService restaurantSpecificationService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.restaurantCreateMapper = restaurantCreateMapper;
        this.restaurantSpecificationService = restaurantSpecificationService;
    }

    @Override
    @Transactional
    public RestaurantDto create(RestaurantCreateDto restaurantCreateDto) {
        Restaurant restaurant = restaurantRepository.save(restaurantCreateMapper.toEntity(restaurantCreateDto));

        LOGGER.info("Rest response for creating Restaurant: {}", restaurantMapper.toDto(restaurant));

        return restaurantMapper.toDto(restaurant);
    }

    @Override
    public List<RestaurantDto> findAll(RestaurantCriteria restaurantCriteria) {
        Specification<Restaurant> specification = restaurantSpecificationService.getAllSpecification(
                restaurantCriteria);

        return restaurantMapper.toDto(restaurantRepository.findAll(specification));
    }

    @Override
    public RestaurantDto findById(Long restaurantId) {

        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(
                        EXCEPTION + restaurantId));

        LOGGER.info("Rest response for finding Restaurant by id: {}", restaurantMapper.toDto(restaurant));

        return restaurantMapper.toDto(restaurant);
    }

    @Override
    @Transactional
    public RestaurantDto updateRestaurant(RestaurantDto restaurantDto) {

        Restaurant currentRestaurant = restaurantRepository
                .findById(restaurantDto.getId())
                .orElseThrow(() -> new NotFoundException(
                        EXCEPTION + restaurantDto.getId()));

        currentRestaurant.setName(restaurantDto.getName());
        currentRestaurant.setRating(restaurantDto.getRating());
        currentRestaurant.setDescription(restaurantDto.getDescription());
        currentRestaurant.setImage(restaurantDto.getImage());

        restaurantRepository.save(currentRestaurant);

        LOGGER.info("Rest response for updating Restaurant: {}", restaurantMapper.toDto(currentRestaurant));

        return restaurantMapper.toDto(currentRestaurant);
    }

    @Override
    @Transactional
    public void delete(Long restaurantId) {

        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(
                        EXCEPTION + restaurantId));

        List<TableInfo> tableInfos = restaurant.getTables();
        for (Iterator<TableInfo> tableInfoIterator = tableInfos.iterator(); tableInfoIterator.hasNext(); ) {
            TableInfo tableInfo = tableInfoIterator.next();
            tableInfo.setRestaurant(null);
            tableInfoIterator.remove(); //remove child relationship with tables
        }

        List<Rating> ratings = restaurant.getRatings();
        for (Iterator<Rating> ratingIterator = ratings.iterator(); ratingIterator.hasNext(); ) {
            Rating rating = ratingIterator.next();
            rating.setRestaurants(null);
            ratingIterator.remove(); //remove child relationship with ratings
        }

        List<Reservation> reservations = restaurant.getReservations();
        for (Iterator<Reservation> reservationIterator = reservations.iterator(); reservationIterator.hasNext(); ) {
            Reservation reservation = reservationIterator.next();
            reservation.setRestaurant(null);
            reservationIterator.remove(); //remove child relationship with reservations
        }

        List<WorkingTime> workingTimes = restaurant.getWorkingTimes();
        for (Iterator<WorkingTime> workingTimeIterator = workingTimes.iterator(); workingTimeIterator.hasNext(); ) {
            WorkingTime workingTime = workingTimeIterator.next();
            workingTime.setRestaurant(null);
            workingTimeIterator.remove(); //remove child relationship with workingTime
        }

        List<NonWorkingDay> nonWorkingDays = restaurant.getNonWorkingDays();
        for (Iterator<NonWorkingDay> nonWorkingDayIterator = nonWorkingDays.iterator(); nonWorkingDayIterator.hasNext(); ) {
            NonWorkingDay nonWorkingDay = nonWorkingDayIterator.next();
            nonWorkingDay.setRestaurant(null);
            nonWorkingDayIterator.remove(); //remove child relationship with workingTime
        }

        restaurantRepository.deleteById(restaurantId);
    }
}
