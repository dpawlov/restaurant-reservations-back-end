package com.service.mapper;

import com.domain.Reservation;
import com.service.dto.ReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TableInfoMapper.class)
public interface ReservationMapper extends BaseMapper<Reservation, ReservationDto> {

    @Override
    @Mapping(target = "tables", source = "tableInfos")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    ReservationDto toDto(Reservation reservation);
}
