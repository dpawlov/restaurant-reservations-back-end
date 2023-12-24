package com.repository;

import com.domain.WorkingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingTimeRepository
        extends JpaRepository<WorkingTime, Long>, JpaSpecificationExecutor<WorkingTime> {
}

