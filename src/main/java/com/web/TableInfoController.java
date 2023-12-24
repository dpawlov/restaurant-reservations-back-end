package com.web;

import com.service.TableInfoService;
import com.service.criteria.TableInfoCriteria;
import com.service.dto.TableInfoCreateDto;
import com.service.dto.TableInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TableInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableInfoController.class);

    private final TableInfoService tableInfoService;

    public TableInfoController(TableInfoService tableInfoService) {
        this.tableInfoService = tableInfoService;
    }

    @PostMapping("/table")
    public TableInfoDto createTable(@RequestBody @Valid TableInfoCreateDto tableInfoCreateDto) {

        LOGGER.info("Rest request for creating TableInfo: " + tableInfoCreateDto.toString());

        return tableInfoService.create(tableInfoCreateDto);
    }

    @GetMapping("/table")
    public List<TableInfoDto> getAllTables(TableInfoCriteria tableInfoCriteria) {

        LOGGER.info("Rest request for getting all Tables");

        return tableInfoService.findAll(tableInfoCriteria);
    }

    @GetMapping("/table/{tableId}")
    public TableInfoDto getTableById(@PathVariable Long tableId) {

        LOGGER.info("Rest request for getting TableInfo by id: " + tableId);

        return tableInfoService.findById(tableId);
    }

    @PutMapping("/table")
    public TableInfoDto updateTable(@RequestBody @Valid TableInfoDto tableInfoDto) {

        LOGGER.info("Rest request for updating TableInfo: " + tableInfoDto.toString());

        return tableInfoService.updateTable(tableInfoDto);
    }

    @DeleteMapping("/table/{tableId}")
    public void deleteTable(@PathVariable Long tableId) {

        LOGGER.info("Rest request for deleting TableInfo by id: " + tableId);

        tableInfoService.deleteTable(tableId);
    }
}
