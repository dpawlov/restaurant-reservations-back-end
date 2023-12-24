package com.service.mapper;

import com.domain.TableInfo;
import com.service.dto.TableInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableInfoMapper extends BaseMapper<TableInfo, TableInfoDto> {

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    TableInfoDto toDto(TableInfo tableInfo);

    @Override
    @Mapping(target = "restaurant.id", source = "restaurantId")
    TableInfo toEntity(TableInfoDto tableInfoDto);

}
