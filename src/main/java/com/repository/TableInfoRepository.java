package com.repository;

import com.domain.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TableInfoRepository
        extends JpaRepository<TableInfo, Long>, JpaSpecificationExecutor<TableInfo> {
}
