package com.service;

import com.service.criteria.NonWorkingDayCriteria;
import com.service.dto.NonWorkingDayCreateDto;
import com.service.dto.NonWorkingDayDto;

import java.util.List;

public interface NonWorkingDayService {
    /**
     * Creates a NonWorkingDay for a restaurant.
     *
     * @param nonWorkingDayCreateDto non-working day details for a restaurant.
     * @return NonWorkingDay entity.
     */
    NonWorkingDayDto create(NonWorkingDayCreateDto nonWorkingDayCreateDto);

    /**
     * Updates a NonWorkingDay for a restaurant.
     *
     * @param nonWorkingDayDto entity which will be used to update the non-working day.
     * @return NonWorkingDay entity.
     */
    NonWorkingDayDto updateNonWorkingDay(NonWorkingDayDto nonWorkingDayDto);

    /**
     * Deletes a NonWorkingDay for a restaurant.
     *
     * @param nonWorkingDayId non-working-day which will be deleted from the Database.
     */
    void deleteNonWorkingDay(Long nonWorkingDayId);

    /**
     * Return all Non-Working Days of the Restaurants in the Database.
     *
     * @return List of NonWorkingDays.
     */
    List<NonWorkingDayDto> findAll(NonWorkingDayCriteria nonWorkingDayCriteria);

    /**
     * Finds a NonWorkingDay by its id.
     *
     * @param nonWorkingDayId non-working-day which will be returned.
     * @return NonWorkingDay entity.
     */
    NonWorkingDayDto findById(Long nonWorkingDayId);
}
