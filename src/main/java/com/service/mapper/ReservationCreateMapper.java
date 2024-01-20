package com.service.mapper;

import com.domain.Reservation;
import com.service.dto.ReservationCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TableInfoMapper.class)
public interface ReservationCreateMapper extends BaseMapper<Reservation, ReservationCreateDto> {

    @Override
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "tableInfos", source = "tables")
    Reservation toEntity(ReservationCreateDto reservationCreateDto);

    @Override
    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tables", source = "tableInfos")
    ReservationCreateDto toDto(Reservation reservation);
}
