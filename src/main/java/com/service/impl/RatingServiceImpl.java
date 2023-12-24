package com.service.impl;

import com.domain.Rating;
import com.domain.Restaurant;
import com.repository.RatingRepository;
import com.repository.RestaurantRepository;
import com.service.RatingService;
import com.service.criteria.RatingCriteria;
import com.service.dto.RatingCreateDto;
import com.service.dto.RatingDto;
import com.service.mapper.RatingCreateMapper;
import com.service.mapper.RatingMapper;
import com.service.specs.RatingSpecificationService;
import com.web.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingServiceImpl.class);
    private static final String EXCEPTION = "Rating with the following id does not exists in the database: ";

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final RatingCreateMapper ratingCreateMapper;
    private final RestaurantRepository restaurantRepository;
    private final RatingSpecificationService ratingSpecificationService;

    public RatingServiceImpl(RatingRepository ratingRepository, RatingMapper ratingMapper, RatingCreateMapper ratingCreateMapper, RestaurantRepository restaurantRepository,
            RatingSpecificationService ratingSpecificationService) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.ratingCreateMapper = ratingCreateMapper;
        this.restaurantRepository = restaurantRepository;
        this.ratingSpecificationService = ratingSpecificationService;
    }

    @Override
    @Transactional
    public RatingDto create(RatingCreateDto ratingCreateDto) {
        Rating rating = ratingCreateMapper.toEntity(ratingCreateDto);

        for (Restaurant restaurant : ratingCreateDto.getRestaurants()) {
            if (restaurantRepository.findById(restaurant.getId()) == null) {
                throw new NotFoundException("Restaurant with the following id: " + restaurant.getId() + " does not exist in the database.");
            }
        }

        rating.setRestaurants(ratingCreateDto.getRestaurants());

        ratingRepository.save(rating);

        LOGGER.info("Rest response for creating Rating: {}", ratingMapper.toDto(rating));

        return ratingMapper.toDto(rating);
    }

    @Override
    public List<RatingDto> findAll(RatingCriteria ratingCriteria) {

        Specification<Rating> specification = ratingSpecificationService.getAllSpecification(ratingCriteria);

        return ratingMapper.toDto(ratingRepository.findAll(specification));
    }

    @Override
    public RatingDto findById(Long ratingId) {

        Rating rating = ratingRepository
                .findById(ratingId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + ratingId));

        LOGGER.info("Rest response for finding Rating by id: {}", ratingMapper.toDto(rating));

        return ratingMapper.toDto(rating);
    }

    @Override
    @Transactional
    public RatingDto updateRating(RatingDto ratingDto) {

        Rating currentRating = ratingRepository
                .findById(ratingDto.getId())
                .orElseThrow(() -> new NotFoundException(EXCEPTION + ratingDto.getId()));

        currentRating.setRatingType(ratingDto.getRatingType());
        currentRating.setDescription(ratingDto.getDescription());

        ratingRepository.save(currentRating);

        LOGGER.info("Rest response for updating Rating: {}", ratingMapper.toDto(currentRating));

        return ratingMapper.toDto(currentRating);
    }

    @Override
    @Transactional
    public void delete(Long ratingId) {

        Rating currentRating = ratingRepository
                .findById(ratingId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + ratingId));

        ratingRepository.delete(currentRating);
    }
}
