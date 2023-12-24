package com.service.mapper;

import com.domain.NonWorkingDay;
import com.service.dto.NonWorkingDayDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NonWorkingDayMapper extends BaseMapper<NonWorkingDay, NonWorkingDayDto> {

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    NonWorkingDayDto toDto(NonWorkingDay nonWorkingDay);

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    List<NonWorkingDayDto> toDto(List<NonWorkingDay> nonWorkingDays);
}
