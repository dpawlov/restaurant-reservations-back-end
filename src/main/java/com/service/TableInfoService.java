package com.service;

import com.service.criteria.TableInfoCriteria;
import com.service.dto.TableInfoCreateDto;
import com.service.dto.TableInfoDto;

import java.util.List;

public interface TableInfoService {

    /**
     * Return all Tables in the Database.
     *
     * @return List of Tables.
     */
    List<TableInfoDto> findAll(TableInfoCriteria tableInfoCriteria);

    /**
     * Creates a Table.
     *
     * @param tableInfoCreateDto the tableInfo details for creating.
     * @return TableInfo entity.
     */
    TableInfoDto create(TableInfoCreateDto tableInfoCreateDto);

    /**
     * Updates a Table.
     *
     * @param tableInfoDto the tableinfo details for updating.
     * @return TableInfo entity.
     */
    TableInfoDto updateTable(TableInfoDto tableInfoDto);

    /**
     * Deletes a Table.
     *
     * @param tableId the table which will be deleted.
     */
    void deleteTable(Long tableId);

    /**
     * Finds a Table by the give id.
     *
     * @param tableId the table which will be returned.
     */
    TableInfoDto findById(Long tableId);
}
