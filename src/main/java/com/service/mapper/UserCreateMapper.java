package com.service.mapper;

import com.domain.User;
import com.service.dto.UserCreateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCreateMapper extends BaseMapper<User, UserCreateDto>{
}
