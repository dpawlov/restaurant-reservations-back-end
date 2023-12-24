package com.web;

import com.service.NonWorkingDayService;
import com.service.criteria.NonWorkingDayCriteria;
import com.service.dto.NonWorkingDayCreateDto;
import com.service.dto.NonWorkingDayDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NonWorkingDayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonWorkingDayController.class);

    private final NonWorkingDayService nonWorkingDayService;

    public NonWorkingDayController(NonWorkingDayService nonWorkingDayService) {
        this.nonWorkingDayService = nonWorkingDayService;
    }

    @PostMapping("/non-working-day")
    public NonWorkingDayDto createNonWorkingDay(@RequestBody @Valid NonWorkingDayCreateDto nonWorkingDayCreateDto) {

        LOGGER.info("Rest request for creating NonWorkingDay: " + nonWorkingDayCreateDto.toString());

        return nonWorkingDayService.create(nonWorkingDayCreateDto);
    }

    @GetMapping("/non-working-day")
    public List<NonWorkingDayDto> getAllNonWorkingDays(NonWorkingDayCriteria nonWorkingDayCriteria) {

        LOGGER.info("Rest request for getting all NonWorkingDays");

        return nonWorkingDayService.findAll(nonWorkingDayCriteria);
    }

    @GetMapping("/non-working-day/{nonWorkingDayId}")
    public NonWorkingDayDto getNonWorkingDayById(@PathVariable Long nonWorkingDayId) {

        LOGGER.info("Rest request for getting NonWorkingDay by id: " + nonWorkingDayId);

        return nonWorkingDayService.findById(nonWorkingDayId);
    }

    @PutMapping("/non-working-day")
    public NonWorkingDayDto updateNonWorkingDay(@RequestBody @Valid NonWorkingDayDto nonWorkingDayDto) {

        LOGGER.info("Rest request for updating NonWorkingDay: " + nonWorkingDayDto.toString());

        return nonWorkingDayService.updateNonWorkingDay(nonWorkingDayDto);
    }

    @DeleteMapping("/non-working-day/{nonWorkingDayId}")
    public void deleteNonWorkingDay(@PathVariable Long nonWorkingDayId) {

        LOGGER.info("Rest request for deleting NonWorkingDay by id: " + nonWorkingDayId);

        nonWorkingDayService.deleteNonWorkingDay(nonWorkingDayId);
    }
}
