package com.service.specs;

import com.domain.WorkingTime_;
import com.service.criteria.WorkingTimeCriteria;
import com.domain.Restaurant;
import com.domain.WorkingTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkingTimeSpecificationService {

    public Specification<WorkingTime> getAllSpecification(WorkingTimeCriteria workingTimeCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (workingTimeCriteria.getDescription() != null) {
                predicates.add(criteriaBuilder.equal(root.get(WorkingTime_.description), workingTimeCriteria.getDescription()));
            }

            if (workingTimeCriteria.getStartTime() != null) {
                predicates.add(criteriaBuilder.equal(root.get(WorkingTime_.startTime), workingTimeCriteria.getStartTime()));
            }

            if (workingTimeCriteria.getEndTime() != null) {
                predicates.add(criteriaBuilder.equal(root.get(WorkingTime_.endTime), workingTimeCriteria.getEndTime()));
            }

            if (workingTimeCriteria.getRestaurantId() != null) {
                Join<WorkingTime, Restaurant> workingTimeRestaurantJoin = root.join(WorkingTime_.restaurant);
                predicates.add(criteriaBuilder.equal(workingTimeRestaurantJoin.get("id"), workingTimeCriteria.getRestaurantId()));
            }

            if (workingTimeCriteria.getDayOfWeek() != null) {
                predicates.add(criteriaBuilder.equal(root.get(WorkingTime_.dayOfWeek), workingTimeCriteria.getDayOfWeek()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

