package com.service.dto;

import javax.validation.constraints.NotNull;

public class TableInfoDto {

    @NotNull(message = "Id cannot be empty!")
    private Long id;

    @NotNull(message = "Table person capacity cannot be empty!")
    private int personCapacity;

    @NotNull(message = "Table number cannot be empty!")
    private int tableNumber;

    private String description;

    @NotNull(message = "RestaurantId cannot be empty!")
    private Long restaurantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPersonCapacity() {
        return personCapacity;
    }

    public void setPersonCapacity(int personCapacity) {
        this.personCapacity = personCapacity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
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
        return "TableInfoDto{" +
                "id=" + id +
                ", personCapacity=" + personCapacity +
                ", tableNumber=" + tableNumber +
                ", description='" + description + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
