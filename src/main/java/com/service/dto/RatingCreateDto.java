package com.service.dto;

import com.domain.Restaurant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class RatingCreateDto {

    @NotNull(message = "Rating type cannot be empty!")
    private String ratingType;

    private String description;

    @Size(min = 1, message = "Minimum size should be at least 1!")
    private List<Restaurant> restaurants;

    public String getRatingType() {
        return ratingType;
    }

    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "RatingCreateDto{" +
                "ratingType='" + ratingType + '\'' +
                ", description='" + description + '\'' +
                ", restaurants=" + restaurants +
                '}';
    }
}
