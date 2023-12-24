package com.service.mapper;

import com.domain.Rating;
import com.service.dto.RatingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper extends BaseMapper<Rating, RatingDto> {
}
