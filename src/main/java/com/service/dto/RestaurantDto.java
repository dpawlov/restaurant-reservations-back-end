package com.service.dto;

import com.domain.TableInfo;
import com.domain.WorkingTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class RestaurantDto {

    @NotNull(message = "Id cannot be empty!")
    private Long id;

    @NotNull(message = "Name cannot be empty!")
    private String name;

    private float rating;

    private String description;

    private String image;

    @Size(min = 1, message = "Minimum size should be at least 1!")
    List<WorkingTime> workingTimes;

    @Size(min = 1, message = "Minimum size should be at least 1!")
    List<TableInfo> tables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<WorkingTime> getWorkingTimes() {
        return workingTimes;
    }

    public void setWorkingTimes(List<WorkingTime> workingTimes) {
        this.workingTimes = workingTimes;
    }

    public List<TableInfo> getTables() {
        return tables;
    }

    public void setTables(List<TableInfo> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "RestaurantDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
