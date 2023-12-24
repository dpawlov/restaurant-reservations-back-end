package com.repository;

import com.domain.NonWorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NonWorkingDayRepository
        extends JpaRepository<NonWorkingDay, Long>, JpaSpecificationExecutor<NonWorkingDay> {
}
