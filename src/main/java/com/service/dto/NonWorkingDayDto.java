package com.service.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class NonWorkingDayDto {

    @NotNull(message = "Id cannot be null!")
    private Long id;

    @NotNull(message = "Day cannot be empty!")
    private LocalDate nonWorkingDayDate;

    private String description;

    @NotNull(message = "RestaurantId cannot be empty!")
    private Long restaurantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNonWorkingDayDate() {
        return nonWorkingDayDate;
    }

    public void setNonWorkingDayDate(LocalDate nonWorkingDayDate) {
        this.nonWorkingDayDate = nonWorkingDayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }


}
