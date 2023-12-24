package com.service.mapper;

import java.util.List;

public interface BaseMapper<T, L> {
    L toDto(T t);

    T toEntity(L l);

    List<L> toDto(List<T> t);

    List<T> toEntity(List<L> l);
}
