package com.service.dto;

import com.domain.enums.EnumDayOfWeek;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

public class WorkingTimeCreateDto {

    @NotNull(message = "Day of week cannot be empty!")
    private EnumDayOfWeek dayOfWeek;

    @NotNull(message = "Start time cannot be empty!")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be empty!")
    private LocalTime endTime;

    private String description;

    @NotNull(message = "RestaurantId cannot be empty!")
    private Long restaurantId;

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

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "WorkingTimeCreateDto{" +
                "dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", description='" + description + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
