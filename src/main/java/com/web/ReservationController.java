package com.web;

import com.service.ReservationService;
import com.service.criteria.ReservationCriteria;
import com.service.dto.ReservationCreateDto;
import com.service.dto.ReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation")
    public ReservationDto createReservation(@RequestBody @Valid ReservationCreateDto reservationCreateDto) {

        LOGGER.info("Rest request for creating Reservation: " + reservationCreateDto.toString());

        return reservationService.create(reservationCreateDto);
    }

    @GetMapping("/reservation")
    public List<ReservationDto> getAllReservations(ReservationCriteria reservationCriteria) {

        LOGGER.info("Rest request for getting all Reservations");

        return reservationService.findAll(reservationCriteria);
    }

    @GetMapping("/reservation/{reservationId}")
    public ReservationDto getReservationById(@PathVariable Long reservationId) {

        LOGGER.info("Rest request for getting Reservation by id: " + reservationId);

        return reservationService.findById(reservationId);
    }

    @PutMapping("/reservation")
    public ReservationDto updateReservation(@RequestBody @Valid ReservationDto reservationDto) {

        LOGGER.info("Rest request for updating Reservation" + reservationDto.toString());

        return reservationService.updateReservation(reservationDto);
    }

    @DeleteMapping("/reservation/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId) {

        LOGGER.info("Rest request for deleting Reservation by id: " + reservationId);

        reservationService.delete(reservationId);
    }
}
