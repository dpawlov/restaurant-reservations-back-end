package com.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class RatingDto {

    @NotNull(message = "Id cannot be null!")
    private Long id;

    @NotNull(message = "Rating type cannot be null!")
    private String ratingType;

    private String description;

    @Size(min = 1, message = "Minimum size should be at least 1!")
    private List<RestaurantDto> restaurants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<RestaurantDto> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantDto> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "RatingDto{" +
                "id=" + id +
                ", ratingType='" + ratingType + '\'' +
                ", description='" + description + '\'' +
                ", restaurants=" + restaurants +
                '}';
    }
}
