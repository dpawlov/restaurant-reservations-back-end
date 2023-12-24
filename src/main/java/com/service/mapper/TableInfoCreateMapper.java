package com.service.mapper;

import com.domain.TableInfo;
import com.service.dto.TableInfoCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableInfoCreateMapper extends BaseMapper<TableInfo, TableInfoCreateDto> {

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    TableInfoCreateDto toDto(TableInfo tableInfo);

    @Override
    @Mapping(target = "restaurant.id", source = "restaurantId")
    TableInfo toEntity(TableInfoCreateDto tableInfoCreateDto);

}
