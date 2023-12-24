package com.web;

import com.service.RatingService;
import com.service.criteria.RatingCriteria;
import com.service.dto.RatingCreateDto;
import com.service.dto.RatingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RatingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/rating")
    public RatingDto createRating(@RequestBody @Valid RatingCreateDto ratingCreateDto) {

        LOGGER.info("Rest request for creating Rating: " + ratingCreateDto.toString());

        return ratingService.create(ratingCreateDto);
    }

    @GetMapping("/rating")
    public List<RatingDto> getAllRatings(RatingCriteria ratingCriteria) {

        LOGGER.info("Rest request for getting all Ratings");

        return ratingService.findAll(ratingCriteria);
    }

    @GetMapping("/rating/{ratingId}")
    public RatingDto getRatingById(@PathVariable Long ratingId) {

        LOGGER.info("Rest request for getting Rating by id: " + ratingId);

        return ratingService.findById(ratingId);
    }

    @PutMapping("/rating")
    public RatingDto updateRating(@RequestBody @Valid RatingDto ratingDto) {

        LOGGER.info("Rest request for updating Rating: " + ratingDto.toString());

        return ratingService.updateRating(ratingDto);
    }

    @DeleteMapping("/rating/{ratingId}")
    public void deleteRating(@PathVariable Long ratingId) {

        LOGGER.info("Rest request for deleting Rating by id: " + ratingId);

        ratingService.delete(ratingId);
    }
}
