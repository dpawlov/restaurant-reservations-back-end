package com.service.specs;

import com.domain.Reservation;
import com.domain.Reservation_;
import com.domain.Restaurant;
import com.domain.TableInfo;
import com.service.criteria.ReservationCriteria;
import com.utils.TimeConverterUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationSpecificationService {

    public Specification<Reservation> getAllSpecification(ReservationCriteria reservationCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (reservationCriteria.getCustomerName() != null) {
                predicates.add(criteriaBuilder.equal(root.get(Reservation_.customerName),
                        reservationCriteria.getCustomerName()));
            }

            if (reservationCriteria.getPersons() != 0) {
                predicates.add(criteriaBuilder.equal(root.get(Reservation_.persons), reservationCriteria.getPersons()));
            }

            if (reservationCriteria.getRestaurantId() != null) {
                Join<Reservation, Restaurant> reservationRestaurantJoin = root.join(Reservation_.restaurant);
                predicates.add(criteriaBuilder.equal(reservationRestaurantJoin.get("id"),
                        reservationCriteria.getRestaurantId()));
            }

            if (reservationCriteria.getTime() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Reservation_.time),
                        TimeConverterUtil.convertLocalDateToInstant(reservationCriteria.getTime())));
                predicates.add(criteriaBuilder.lessThan(root.get(Reservation_.time),
                        TimeConverterUtil.convertLocalDateToInstant(reservationCriteria.getTime().plus(1, ChronoUnit.DAYS))));
            }

            if (reservationCriteria.getTableInfoId() != null) {
                Join<Reservation, TableInfo> reservationTableInfoJoin = root.join(Reservation_.tableInfos);
                predicates.add(criteriaBuilder.equal(reservationTableInfoJoin.get("id"),
                        reservationCriteria.getTableInfoId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}