package com.service.mapper;

import com.domain.WorkingTime;
import com.service.dto.WorkingTimeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkingTimeMapper extends BaseMapper<WorkingTime, WorkingTimeDto> {

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    WorkingTimeDto toDto(WorkingTime workingTime);

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    List<WorkingTimeDto> toDto(List<WorkingTime> workingTime);

}
