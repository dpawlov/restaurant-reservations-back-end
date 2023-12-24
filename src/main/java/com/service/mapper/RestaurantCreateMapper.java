package com.service.mapper;

import com.domain.Restaurant;
import com.service.dto.RestaurantCreateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantCreateMapper extends BaseMapper<Restaurant, RestaurantCreateDto> {
}
