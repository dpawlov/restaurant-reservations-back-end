package com.service;

import com.service.criteria.WorkingTimeCriteria;
import com.service.dto.WorkingTimeCreateDto;
import com.service.dto.WorkingTimeDto;

import java.util.List;

public interface WorkingTimeService {

    /**
     * Creates a WorkingTime of a Restaurant.
     *
     * @param workingTimeCreateDto the working-time info for creating .
     * @return WorkingTime entity.
     */
    WorkingTimeDto create(WorkingTimeCreateDto workingTimeCreateDto);

    /**
     * Return all WorkingTimes of the Restaurants.
     *
     * @return List of WorkingTimes.
     */
    List<WorkingTimeDto> findAll(WorkingTimeCriteria workingTimeCriteria);

    /**
     * Updates the WorkingTime.
     *
     * @param workingTimeDto the WorkingTime details for updating.
     * @return WorkingTime entity.
     */
    WorkingTimeDto updateWorkingTime(WorkingTimeDto workingTimeDto);

    /**
     * Deletes a WorkingTime.
     *
     * @param workingTimeId the WorkingTime which will be deleted.
     */
    void deleteWorkingTime(Long workingTimeId);

    /**
     * Finds the WorkingTime of a Restaurant, by its id.
     *
     * @param workingTimeId the WorkingTime which will be returned.
     * @return WorkingTime entity.
     */
    WorkingTimeDto findById(Long workingTimeId);

}
