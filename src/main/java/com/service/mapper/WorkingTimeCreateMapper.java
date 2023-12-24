package com.service.mapper;

import com.domain.WorkingTime;
import com.service.dto.WorkingTimeCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkingTimeCreateMapper extends BaseMapper<WorkingTime, WorkingTimeCreateDto> {

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    WorkingTimeCreateDto toDto(WorkingTime workingTime);

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    List<WorkingTimeCreateDto> toDto(List<WorkingTime> workingTime);

}
