package com.service.dto;

import com.domain.enums.EnumDayOfWeek;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

public class WorkingTimeDto {

    @NotNull(message = "WorkingTime id cannot be empty!")
    private Long id;

    @NotNull(message = "Start time cannot be empty!")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be empty!")
    private LocalTime endTime;

    @NotNull(message = "RestaurantId cannot be empty!")
    private Long restaurantId;

    private String description;

    @NotNull(message = "Day of week cannot be empty!")
    private EnumDayOfWeek dayOfWeek;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EnumDayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(EnumDayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return "WorkingTimeDto{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", restaurantId=" + restaurantId +
                ", description='" + description + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }
}
