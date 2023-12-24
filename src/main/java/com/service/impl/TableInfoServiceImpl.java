package com.service.impl;

import com.domain.Restaurant;
import com.domain.TableInfo;
import com.repository.RestaurantRepository;
import com.repository.TableInfoRepository;
import com.service.TableInfoService;
import com.service.criteria.TableInfoCriteria;
import com.service.dto.TableInfoCreateDto;
import com.service.dto.TableInfoDto;
import com.service.mapper.TableInfoCreateMapper;
import com.service.mapper.TableInfoMapper;
import com.service.specs.TableInfoSpecificationService;
import com.web.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TableInfoServiceImpl implements TableInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableInfoServiceImpl.class);
    private static final String EXCEPTION = "Table with the following id does not exists in the database: ";

    private final TableInfoRepository tableInfoRepository;
    private final RestaurantRepository restaurantRepository;
    private final TableInfoCreateMapper tableInfoCreateMapper;
    private final TableInfoMapper tableInfoMapper;
    private final TableInfoSpecificationService tableInfoSpecificationService;

    public TableInfoServiceImpl(TableInfoRepository tableInfoRepository, RestaurantRepository restaurantRepository, TableInfoCreateMapper tableInfoCreateMapper, TableInfoMapper tableInfoMapper,
            TableInfoSpecificationService tableInfoSpecificationService) {
        this.tableInfoRepository = tableInfoRepository;
        this.restaurantRepository = restaurantRepository;
        this.tableInfoCreateMapper = tableInfoCreateMapper;
        this.tableInfoMapper = tableInfoMapper;
        this.tableInfoSpecificationService = tableInfoSpecificationService;
    }

    @Override
    @Transactional
    public TableInfoDto create(TableInfoCreateDto tableInfoCreateDto) {

        Restaurant currentRestaurant = restaurantRepository
                .findById(tableInfoCreateDto.getRestaurantId())
                .orElseThrow(() -> new NotFoundException(EXCEPTION +  tableInfoCreateDto.getRestaurantId()));

        TableInfo tableInfo = tableInfoCreateMapper.toEntity(tableInfoCreateDto);

        tableInfo.setRestaurant(currentRestaurant);

        tableInfoRepository.save(tableInfo);

        LOGGER.info("Rest response for creating TableInfo: {}", tableInfoMapper.toDto(tableInfo));

        return tableInfoMapper.toDto(tableInfo);
    }

    @Override
    public List<TableInfoDto> findAll(TableInfoCriteria tableInfoCriteria) {
        Specification<TableInfo> specification = tableInfoSpecificationService.getAllSpecification(tableInfoCriteria);

        return tableInfoMapper.toDto(tableInfoRepository.findAll(specification));
    }

    @Override
    public TableInfoDto findById(Long tableId) {

        TableInfo tableInfo = tableInfoRepository
                .findById(tableId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + tableId));

        LOGGER.info("Rest response for finding TableInfo by id: {}", tableInfoMapper.toDto(tableInfo));

        return tableInfoMapper.toDto(tableInfo);
    }

    @Override
    @Transactional
    public TableInfoDto updateTable(TableInfoDto tableInfoDto) {

        TableInfo currentTable = tableInfoRepository
                .findById(tableInfoDto.getId())
                .orElseThrow(() -> new NotFoundException(EXCEPTION + tableInfoDto.getId()));

        currentTable.setTableNumber(tableInfoDto.getTableNumber());
        currentTable.setPersonCapacity(tableInfoDto.getPersonCapacity());
        currentTable.setDescription(tableInfoDto.getDescription());

        tableInfoRepository.save(currentTable);

        LOGGER.info("Rest response for updating TableInfo: {}", tableInfoMapper.toDto(currentTable));

        return tableInfoMapper.toDto(currentTable);
    }

    @Override
    @Transactional
    public void deleteTable(Long tableId) {

        TableInfo tableToBeRemoved = tableInfoRepository
                .findById(tableId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + tableId));

        Restaurant currentRestaurant = tableToBeRemoved.getRestaurant();

        currentRestaurant.getTables().remove(tableToBeRemoved);

        tableInfoRepository.delete(tableToBeRemoved);
    }
}
