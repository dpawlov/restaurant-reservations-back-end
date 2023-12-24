package com.service.dto;

import javax.validation.constraints.NotNull;

public class RestaurantCreateDto {

    @NotNull(message = "Name cannot be empty!")
    private String name;

    @NotNull(message = "Rating cannot be empty!")
    private float rating;

    private String description;

    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "RestaurantCreateDto{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
