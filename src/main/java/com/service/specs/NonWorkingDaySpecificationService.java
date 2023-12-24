package com.service.specs;

import com.domain.NonWorkingDay_;
import com.service.criteria.NonWorkingDayCriteria;
import com.domain.NonWorkingDay;
import com.domain.Restaurant;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NonWorkingDaySpecificationService {

    public Specification<NonWorkingDay> getAllSpecification(NonWorkingDayCriteria nonWorkingDayCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nonWorkingDayCriteria.getDescription() != null) {
                predicates.add(criteriaBuilder.equal(root.get(NonWorkingDay_.description), nonWorkingDayCriteria.getDescription()));
            }

            if (nonWorkingDayCriteria.getRestaurantId() != null) {
                Join<NonWorkingDay, Restaurant> nonWorkingDayRestaurantJoin = root.join(NonWorkingDay_.restaurant);
                predicates.add(criteriaBuilder.equal(nonWorkingDayRestaurantJoin.get("id"), nonWorkingDayCriteria.getRestaurantId()));
            }

            if (nonWorkingDayCriteria.getNonWorkingDayDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get(NonWorkingDay_.nonWorkingDayDate), nonWorkingDayCriteria.getNonWorkingDayDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
