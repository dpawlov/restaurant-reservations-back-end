package com.service.mapper;

import com.domain.NonWorkingDay;
import com.service.dto.NonWorkingDayCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NonWorkingDayCreateMapper extends BaseMapper<NonWorkingDay, NonWorkingDayCreateDto> {

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    NonWorkingDayCreateDto toDto(NonWorkingDay nonWorkingDay);

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    List<NonWorkingDayCreateDto> toDto(List<NonWorkingDay> nonWorkingDays);

}
