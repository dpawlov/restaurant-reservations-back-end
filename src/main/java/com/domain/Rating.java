package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "restaurant_rating",
            joinColumns = { @JoinColumn(name = "rating_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "restaurant_id", referencedColumnName = "id") })
    private List<Restaurant> restaurants = new ArrayList<>();

    @Column(name = "rating_type", nullable = false)
    private String ratingType;

    @Column(name = "description")
    private String description;

    public Rating() {
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

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
}
