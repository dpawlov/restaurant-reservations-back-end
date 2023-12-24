package com.service.impl;

import com.domain.NonWorkingDay;
import com.domain.Restaurant;
import com.repository.NonWorkingDayRepository;
import com.repository.RestaurantRepository;
import com.service.NonWorkingDayService;
import com.service.criteria.NonWorkingDayCriteria;
import com.service.dto.NonWorkingDayCreateDto;
import com.service.dto.NonWorkingDayDto;
import com.service.mapper.NonWorkingDayCreateMapper;
import com.service.mapper.NonWorkingDayMapper;
import com.service.specs.NonWorkingDaySpecificationService;
import com.web.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NonWorkingDayServiceImpl implements NonWorkingDayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonWorkingDayServiceImpl.class);
    private static final String EXCEPTION = "NonWorkingDay with the following id does not exists in the database: ";

    private final NonWorkingDayRepository nonWorkingDayRepository;
    private final RestaurantRepository restaurantRepository;
    private final NonWorkingDayCreateMapper nonWorkingDayCreateMapper;
    private final NonWorkingDayMapper nonWorkingDayMapper;
    private final NonWorkingDaySpecificationService nonWorkingDaySpecificationService;

    public NonWorkingDayServiceImpl(NonWorkingDayRepository nonWorkingDayRepository, RestaurantRepository restaurantRepository, NonWorkingDayCreateMapper nonWorkingDayCreateMapper, NonWorkingDayMapper nonWorkingDayMapper,
            NonWorkingDaySpecificationService nonWorkingDaySpecificationService) {
        this.nonWorkingDayRepository = nonWorkingDayRepository;
        this.restaurantRepository = restaurantRepository;
        this.nonWorkingDayCreateMapper = nonWorkingDayCreateMapper;
        this.nonWorkingDayMapper = nonWorkingDayMapper;
        this.nonWorkingDaySpecificationService = nonWorkingDaySpecificationService;
    }

    @Override
    @Transactional
    public NonWorkingDayDto create(NonWorkingDayCreateDto nonWorkingDayCreateDto) {

        Restaurant currentRestaurant = restaurantRepository
                .findById(nonWorkingDayCreateDto.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant with the following id: " + nonWorkingDayCreateDto.getRestaurantId() + " does not exists in the database."));

        NonWorkingDay nonWorkingDay = nonWorkingDayCreateMapper.toEntity(nonWorkingDayCreateDto);

        nonWorkingDay.setRestaurant(currentRestaurant);

        nonWorkingDayRepository.save(nonWorkingDay);

        LOGGER.info("Rest response for creating NonWorkingDay: {}", nonWorkingDayMapper.toDto(nonWorkingDay));

        return nonWorkingDayMapper.toDto(nonWorkingDay);
    }


    @Override
    public List<NonWorkingDayDto> findAll(NonWorkingDayCriteria nonWorkingDayCriteria) {

        Specification<NonWorkingDay> specification = nonWorkingDaySpecificationService.getAllSpecification(nonWorkingDayCriteria);

        return nonWorkingDayMapper.toDto(nonWorkingDayRepository.findAll(specification));
    }

    @Override
    public NonWorkingDayDto findById(Long nonWorkingDayId) {

        NonWorkingDay nonWorkingDay = nonWorkingDayRepository
                .findById(nonWorkingDayId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + nonWorkingDayId));


        LOGGER.info("Rest response for finding NonWorkingDay by id: {}", nonWorkingDayMapper.toDto(nonWorkingDay));

        return nonWorkingDayMapper.toDto(nonWorkingDay);
    }

    @Override
    @Transactional
    public NonWorkingDayDto updateNonWorkingDay(NonWorkingDayDto nonWorkingDayDto) {

        NonWorkingDay currentNonWorkingDay = nonWorkingDayRepository
                .findById(nonWorkingDayDto.getId())
                .orElseThrow(() -> new NotFoundException(EXCEPTION + nonWorkingDayDto.getId()));

        currentNonWorkingDay.setNonWorkingDayDate(nonWorkingDayDto.getNonWorkingDayDate());

        currentNonWorkingDay.setDescription(nonWorkingDayDto.getDescription());

        nonWorkingDayRepository.save(currentNonWorkingDay);

        LOGGER.info("Rest response for updating NonWorkingDay: {}", nonWorkingDayMapper.toDto(currentNonWorkingDay));

        return nonWorkingDayMapper.toDto(currentNonWorkingDay);
    }

    @Override
    @Transactional
    public void deleteNonWorkingDay(Long nonWorkingDayId) {

        NonWorkingDay nonWorkingDayToBeRemoved = nonWorkingDayRepository
                .findById(nonWorkingDayId)
                .orElseThrow(() -> new NotFoundException(EXCEPTION + nonWorkingDayId));

        Restaurant currentRestaurant = nonWorkingDayToBeRemoved.getRestaurant();

        currentRestaurant.getNonWorkingDays().remove(nonWorkingDayToBeRemoved);

        nonWorkingDayRepository.delete(nonWorkingDayToBeRemoved);
    }
}
