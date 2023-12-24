package com.service.mapper;

import com.domain.Rating;
import com.service.dto.RatingCreateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingCreateMapper extends BaseMapper<Rating, RatingCreateDto> {
}
