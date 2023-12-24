package com.service.specs;

import com.domain.Rating_;
import com.service.criteria.RatingCriteria;
import com.domain.Rating;
import com.domain.Restaurant;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingSpecificationService {

    public Specification<Rating> getAllSpecification(RatingCriteria ratingCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ratingCriteria.getDescription() != null) {
                predicates.add(criteriaBuilder.equal(root.get(Rating_.description), ratingCriteria.getDescription()));
            }

            if (ratingCriteria.getRestaurantId() != null) {
                Join<Rating, Restaurant> ratingRestaurantJoin = root.join(Rating_.restaurants);
                predicates.add(criteriaBuilder.equal(ratingRestaurantJoin.get("id"), ratingCriteria.getRestaurantId()));
            }

            if (ratingCriteria.getRatingType() != null) {
                predicates.add(criteriaBuilder.equal(root.get(Rating_.ratingType), ratingCriteria.getRatingType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}