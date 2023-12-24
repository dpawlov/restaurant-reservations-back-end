package com.service;

import com.service.criteria.RatingCriteria;
import com.service.dto.RatingCreateDto;
import com.service.dto.RatingDto;

import java.util.List;

public interface RatingService {
    /**
     * Creates a Rating for a restaurant.
     *
     * @param ratingCreateDto rating details which will be saved in the DB.
     * @return Rating entity.
     */
    RatingDto create(RatingCreateDto ratingCreateDto);

    /**
     * Return all Ratings in the Database.
     *
     * @return List of Ratings.
     */
    List<RatingDto> findAll(RatingCriteria ratingCriteria);

    /**
     * Updates a Rating for a restaurant.
     *
     * @param ratingDto rating details, used to update the given restaurant rating.
     * @return Rating entity.
     */
    RatingDto updateRating(RatingDto ratingDto);

    /**
     * Deletes a Rating for a restaurant.
     *
     * @param ratingId the rating which will be deleted.
     */
    void delete(Long ratingId);

    /**
     * Finds a Rating by its id.
     *
     * @param ratingId the rating which will be returned.
     * @return Rating entity.
     */
    RatingDto findById(Long ratingId);
}
