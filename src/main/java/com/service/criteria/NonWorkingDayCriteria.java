package com.service.criteria;

import java.time.LocalDate;

public class NonWorkingDayCriteria {

    private Long restaurantId;

    private LocalDate nonWorkingDayDate;

    private String description;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
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
}
