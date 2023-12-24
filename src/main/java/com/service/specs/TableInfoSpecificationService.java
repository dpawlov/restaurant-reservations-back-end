package com.service.specs;

import com.domain.Reservation;
import com.domain.TableInfo_;
import com.service.criteria.TableInfoCriteria;
import com.domain.Restaurant;
import com.domain.TableInfo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableInfoSpecificationService {

    public Specification<TableInfo> getAllSpecification(TableInfoCriteria tableInfoCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tableInfoCriteria.getDescription() != null) {
                predicates.add(criteriaBuilder.equal(root.get(TableInfo_.description), tableInfoCriteria.getDescription()));
            }

            if (tableInfoCriteria.getPersonCapacity() != 0) {
                predicates.add(criteriaBuilder.equal(root.get(TableInfo_.personCapacity), tableInfoCriteria.getPersonCapacity()));
            }

            if (tableInfoCriteria.getTableNumber() != 0) {
                predicates.add(criteriaBuilder.equal(root.get(TableInfo_.tableNumber), tableInfoCriteria.getTableNumber()));
            }

            if (tableInfoCriteria.getRestaurantId() != null) {
                Join<TableInfo, Restaurant> tableInfoRestaurantJoin = root.join(TableInfo_.restaurant);
                predicates.add(criteriaBuilder.equal(tableInfoRestaurantJoin.get("id"), tableInfoCriteria.getRestaurantId()));
            }

            if (tableInfoCriteria.getReservationId() != null) {
                Join<TableInfo, Reservation> tableInfoReservationJoin = root.join(TableInfo_.reservations);
                predicates.add(criteriaBuilder.equal(tableInfoReservationJoin.get("id"), tableInfoCriteria.getReservationId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

