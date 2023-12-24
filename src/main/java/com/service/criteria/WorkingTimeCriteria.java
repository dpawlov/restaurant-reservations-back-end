package com.service.criteria;

import com.domain.enums.EnumDayOfWeek;

import java.time.LocalTime;

public class WorkingTimeCriteria {

    private Long restaurantId;

    private EnumDayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private String description;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public EnumDayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(EnumDayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
