package com.service.specs;

import com.domain.Restaurant;
import com.domain.Restaurant_;
import com.service.criteria.RestaurantCriteria;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantSpecificationService {

    public Specification<Restaurant> getAllSpecification(RestaurantCriteria restaurantCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (restaurantCriteria.getDescription() != null) {
                predicates.add(criteriaBuilder.equal(root.get(Restaurant_.description), restaurantCriteria.getDescription()));
            }

            if (restaurantCriteria.getName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Restaurant_.name)), "%"+restaurantCriteria.getName().toLowerCase()+"%"));
            }

            if (restaurantCriteria.getImage() != null) {
                predicates.add(criteriaBuilder.equal(root.get(Restaurant_.image), restaurantCriteria.getImage()));
            }

            if (restaurantCriteria.getRating() == -1) {
                query.orderBy(criteriaBuilder.asc(root.get(Restaurant_.rating)));
            }

            if (restaurantCriteria.getRating() == -2) {
                query.orderBy(criteriaBuilder.desc(root.get(Restaurant_.rating)));
            }

            if (restaurantCriteria.getRating() != -1 && restaurantCriteria.getRating() != -2 && restaurantCriteria.getRating() != 0) {
                predicates.add(criteriaBuilder.equal(root.get(Restaurant_.rating), restaurantCriteria.getRating()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}