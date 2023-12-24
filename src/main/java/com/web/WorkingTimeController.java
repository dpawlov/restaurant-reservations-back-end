package com.web;

import com.service.WorkingTimeService;
import com.service.criteria.WorkingTimeCriteria;
import com.service.dto.WorkingTimeCreateDto;
import com.service.dto.WorkingTimeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkingTimeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingTimeController.class);

    private final WorkingTimeService workingTimeService;

    public WorkingTimeController(WorkingTimeService workingTimeService) {
        this.workingTimeService = workingTimeService;
    }

    @PostMapping("/working-time")
    public WorkingTimeDto createWorkingTime(@RequestBody @Valid WorkingTimeCreateDto workingTimeCreateDto) {

        LOGGER.info("Rest request for creating WorkingTime: " + workingTimeCreateDto.toString());

        return workingTimeService.create(workingTimeCreateDto);
    }

    @GetMapping("/working-time/{workingTimeId}")
    public WorkingTimeDto getWorkingTimeById(@PathVariable Long workingTimeId) {

        LOGGER.info("Rest request for getting WorkingTime by id: " + workingTimeId);

        return workingTimeService.findById(workingTimeId);
    }

    @GetMapping("/working-time")
    public List<WorkingTimeDto> getAllWorkingTimes(WorkingTimeCriteria workingTimeCriteria) {

        LOGGER.info("Rest request for getting all WorkingTimes");

        return workingTimeService.findAll(workingTimeCriteria);
    }

    @PutMapping("/working-time")
    public WorkingTimeDto updateWorkingTime(@RequestBody @Valid WorkingTimeDto workingTimeDto) {

        LOGGER.info("Rest request for updating WorkingTime: " + workingTimeDto.toString());

        return workingTimeService.updateWorkingTime(workingTimeDto);
    }

    @DeleteMapping("/working-time/{workingTimeId}")
    public void deleteWorkingTime(@PathVariable Long workingTimeId) {

        LOGGER.info("Rest request for deleting WorkingTime by id: " + workingTimeId);

        workingTimeService.deleteWorkingTime(workingTimeId);
    }
}
