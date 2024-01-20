package com.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public class ReservationDto {

    @NotNull(message = "Id cannot be empty!")
    private Long id;

    @NotNull(message = "Time cannot be empty!")
    private Instant time;

    private boolean isCompleted;

    @NotNull(message = "Customer name cannot be empty!")
    private String customerName;

    @NotNull(message = "Customer phone name cannot be empty!")
    private String customerPhone;

    @NotNull(message = "Persons cannot be null!")
    private int persons;

    @Size(min = 1, message = "Minimum size should be at least 1!")
    private List<TableInfoDto> tables;

    @NotNull(message = "RestaurantId cannot be empty!")
    private Long restaurantId;

    @NotNull(message = "UserId cannot be empty!")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public List<TableInfoDto> getTables() {
        return tables;
    }

    public void setTables(List<TableInfoDto> tables) {
        this.tables = tables;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
                ", time=" + time +
                ", isCompleted=" + isCompleted +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", persons=" + persons +
                ", tables=" + tables +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                '}';
    }
}
