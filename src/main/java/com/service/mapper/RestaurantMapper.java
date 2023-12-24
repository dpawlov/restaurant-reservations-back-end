package com.service.mapper;

import com.domain.Restaurant;
import com.service.dto.RestaurantDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper extends BaseMapper<Restaurant, RestaurantDto> {

    @Override
    @Mapping(target = "workingTimes", source = "workingTimes")
    @Mapping(target = "tables", source = "tables")
    RestaurantDto toDto(Restaurant restaurant);
}
