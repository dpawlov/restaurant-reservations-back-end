package com.service;

import com.service.criteria.ReservationCriteria;
import com.service.dto.ReservationCreateDto;
import com.service.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    /**
     * Creates a Reservation for a table.
     *
     * @param reservationCreateDto the reservation details which will be saved in the database.
     * @return Reservation entity.
     */
    ReservationDto create(ReservationCreateDto reservationCreateDto);

    /**
     * Return all Reservations in the Database.
     * @return List of Reservations.
     */
    List<ReservationDto> findAll(ReservationCriteria reservationCriteria);

    /**
     * Updates a Reservation.
     *
     * @param reservationDto the reservation details, which will be updated.
     * @return Reservation entity.
     */
    ReservationDto updateReservation(ReservationDto reservationDto);


    /**
     * Deletes a Reservation for a table.
     *
     * @param reservationId the reservation which will be deleted.
     */
    void delete(Long reservationId);

    /**
     * Finds a Reservation by its id.
     *
     * @param reservationId the reservation which will be returned.
     * @return Reservation entity.
     */
    ReservationDto findById(Long reservationId);
}
