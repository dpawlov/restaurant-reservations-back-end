package com.service.impl;

import com.domain.Restaurant;
import com.domain.WorkingTime;
import com.repository.RestaurantRepository;
import com.repository.WorkingTimeRepository;
import com.service.WorkingTimeService;
import com.service.criteria.WorkingTimeCriteria;
import com.service.dto.WorkingTimeCreateDto;
import com.service.dto.WorkingTimeDto;
import com.service.mapper.WorkingTimeCreateMapper;
import com.service.mapper.WorkingTimeMapper;
import com.service.specs.WorkingTimeSpecificationService;
import com.web.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class WorkingTimeServiceImpl implements WorkingTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingTimeServiceImpl.class);
    private static final String EXCEPTION = "WorkingTime with the following id does not exists in the database: ";

    private final WorkingTimeRepository workingTimeRepository;
    private final RestaurantRepository restaurantRepository;
    private final WorkingTimeMapper workingTimeMapper;
    private final WorkingTimeCreateMapper workingTimeCreateMapper;
    private final WorkingTimeSpecificationService workingTimeSpecificationService;

    public WorkingTimeServiceImpl(WorkingTimeRepository workingTimeRepository, RestaurantRepository restaurantRepository, WorkingTimeMapper workingTimeMapper, WorkingTimeCreateMapper workingTimeCreateMapper,
            WorkingTimeSpecificationService workingTimeSpecificationService) {
        this.workingTimeRepository = workingTimeRepository;
        this.restaurantRepository = restaurantRepository;
        this.workingTimeMapper = workingTimeMapper;
        this.workingTimeCreateMapper = workingTimeCreateMapper;
        this.workingTimeSpecificationService = workingTimeSpecificationService;
    }

    @Override
    @Transactional
    public WorkingTimeDto create(WorkingTimeCreateDto workingTimeCreateDto) {

        Restaurant currentRestaurant = restaurantRepository
                .findById(workingTimeCreateDto.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant with the following id: " + workingTimeCreateDto.getRestaurantId() + " does not exists in the database."));

        WorkingTime workingTime = workingTimeCreateMapper.toEntity(workingTimeCreateDto);

        workingTime.setRestaurant(currentRestaurant);

        workingTimeRepository.save(workingTime);

        LOGGER.info("Rest response for creating WorkingTime: {}", workingTimeMapper.toDto(workingTime));

        return workingTimeMapper.toDto(workingTime);
    }

    @Override
    public List<WorkingTimeDto> findAll(WorkingTimeCriteria workingTimeCriteria) {

        Specification<WorkingTime> specification = workingTimeSpecificationService.getAllSpecification(workingTimeCriteria);

        return workingTimeMapper.toDto(workingTimeRepository.findAll(specification));
    }

    @Override
    public WorkingTimeDto findById(Long workingTimeId) {

        WorkingTime workingTime = workingTimeRepository
                .findById(workingTimeId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + workingTimeId));

        LOGGER.info("Rest response for finding WorkingTime by id: {}", workingTimeMapper.toDto(workingTime));

        return workingTimeMapper.toDto(workingTime);
    }

    @Override
    @Transactional
    public WorkingTimeDto updateWorkingTime(WorkingTimeDto workingTimeDto) {

        WorkingTime currentWorkingTime = workingTimeRepository
                .findById(workingTimeDto.getId())
                .orElseThrow(() -> new NotFoundException(EXCEPTION + workingTimeDto.getId()));

        currentWorkingTime.setStartTime(workingTimeDto.getStartTime());
        currentWorkingTime.setEndTime(workingTimeDto.getEndTime());
        currentWorkingTime.setDescription(workingTimeDto.getDescription());
        currentWorkingTime.setDayOfWeek(workingTimeDto.getDayOfWeek());

        workingTimeRepository.save(currentWorkingTime);

        LOGGER.info("Rest response for updating WorkingTime: {}", workingTimeMapper.toDto(currentWorkingTime));

        return workingTimeMapper.toDto(currentWorkingTime);
    }

    @Override
    @Transactional
    public void deleteWorkingTime(Long workingTimeId) {

        WorkingTime workingTimeToBeRemoved = workingTimeRepository
                .findById(workingTimeId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + workingTimeId));

        Restaurant currentRestaurant = workingTimeToBeRemoved.getRestaurant();

        currentRestaurant.getWorkingTimes().remove(workingTimeToBeRemoved);

        workingTimeRepository.delete(workingTimeToBeRemoved);
    }
}
