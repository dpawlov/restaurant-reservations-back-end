package com.service.impl;

import com.domain.*;
import com.repository.ReservationRepository;
import com.repository.RestaurantRepository;
import com.repository.TableInfoRepository;
import com.repository.UserRepository;
import com.service.criteria.ReservationCriteria;
import com.service.specs.ReservationSpecificationService;
import com.service.ReservationService;
import com.service.dto.ReservationCreateDto;
import com.service.dto.ReservationDto;
import com.service.mapper.ReservationCreateMapper;
import com.service.mapper.ReservationMapper;
import com.utils.CustomUserDetails;
import com.utils.JwtTokenUtil;
import com.web.errors.BadRequestException;
import com.web.errors.NotFoundException;
import com.utils.TimeConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private static final String TABLE_RESERVATION_INVALID_TIME_SLOT = "Table cannot be reserved, because the time slot is not free.";

    private final ReservationRepository reservationRepository;
    private final ReservationCreateMapper reservationCreateMapper;
    private final ReservationMapper reservationMapper;
    private final RestaurantRepository restaurantRepository;
    private final TableInfoRepository tableInfoRepository;
    private final ReservationSpecificationService reservationSpecificationService;
    private final UserRepository userRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservationCreateMapper reservationCreateMapper, ReservationMapper reservationMapper,
                                  RestaurantRepository restaurantRepository, TableInfoRepository tableInfoRepository,
                                  ReservationSpecificationService reservationSpecificationService, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationCreateMapper = reservationCreateMapper;
        this.reservationMapper = reservationMapper;
        this.restaurantRepository = restaurantRepository;
        this.tableInfoRepository = tableInfoRepository;
        this.reservationSpecificationService = reservationSpecificationService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ReservationDto create(ReservationCreateDto reservationCreateDto) {
        User user = userRepository.findById(reservationCreateDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Reservation reservation = reservationCreateMapper.toEntity(reservationCreateDto);

        Restaurant currentRestaurant = restaurantRepository.findById(reservation.getRestaurant().getId()).orElseThrow(
                () -> new NotFoundException("Restaurant with the following id: " + reservation.getRestaurant()
                        .getId() + " does not exist in the database."));

        WorkingTime workingTime = workingTimeValidation(reservation, currentRestaurant);

        restaurantWorkingTimeValidation(reservation, workingTime, currentRestaurant);

        for (TableInfo tableInfo : reservation.getTableInfos()) {

            TableInfo dbTableInfo = tableInfoRepository.findById(tableInfo.getId()).orElseThrow(
                    () -> new NotFoundException(
                            "Table with the following id: " + tableInfo.getId() + " does not exist in the database."));

            if (!dbTableInfo.getRestaurant().getId().equals(reservationCreateDto.getRestaurantId())) {
                throw new BadRequestException("The tables you are trying to reserve are not in the same restaurant.");
            }

            List<Instant> reservationTimes = currentRestaurant.getReservations().stream().map(Reservation::getTime)
                    .collect(Collectors.toList());

            reservationTimeValidation(reservation, reservationTimes);
        }

        reservation.setCompleted(true);

        reservation.setUser(user);

        reservationRepository.save(reservation);

        LOGGER.info("Rest response for creating Reservation by id: {}", reservationMapper.toDto(reservation));

        return reservationMapper.toDto(reservation);
    }

    @Override
    public List<ReservationDto> findAll(ReservationCriteria reservationCriteria) {

        Specification<Reservation> specification = reservationSpecificationService.getAllSpecification(
                reservationCriteria);

        return reservationMapper.toDto(reservationRepository.findAll(specification));
    }

    @Override
    public ReservationDto findById(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NotFoundException(
                String.format("Reservation with the following id: %s does not exists in the database.",
                        reservationId)));

        LOGGER.info("Rest response for finding Reservation by id: {}", reservationMapper.toDto(reservation));

        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional
    public ReservationDto updateReservation(ReservationDto reservationDto) {

        Reservation currentReservation = reservationRepository.findById(reservationDto.getId()).orElseThrow(
                () -> new NotFoundException(
                        "Reservation with the following id: " + reservationDto.getId() + " does not exist in the database."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("Authentication principal not found");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userIdFromPrincipal = customUserDetails.getId();

        if (!currentReservation.getUser().getId().equals(userIdFromPrincipal)) {
            throw new AccessDeniedException("User not authorized to edit this reservation");
        }

        currentReservation.setCustomerName(reservationDto.getCustomerName());
        currentReservation.setCustomerPhone(reservationDto.getCustomerPhone());
        currentReservation.setPersons(reservationDto.getPersons());
        currentReservation.setTime(reservationDto.getTime());
        currentReservation.setCompleted(true);

        reservationRepository.save(currentReservation);

        LOGGER.info("Rest response for updating Reservation: {}", reservationMapper.toDto(currentReservation));

        return reservationMapper.toDto(currentReservation);
    }

    @Override
    @Transactional
    public void delete(Long reservationId) {

        Reservation currentReservation  = reservationRepository.findById(reservationId).orElseThrow(() -> new NotFoundException(
                "Reservation with the following id: " + reservationId + " does not exists in the database."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("Authentication principal not found");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userIdFromPrincipal = customUserDetails.getId();

        if (!currentReservation.getUser().getId().equals(userIdFromPrincipal)) {
            throw new AccessDeniedException("User not authorized to delete this reservation");
        }

        if (Objects.nonNull(currentReservation )) {
            List<TableInfo> tableInfos = currentReservation .getTableInfos();
            for (Iterator<TableInfo> iterator = tableInfos.iterator(); iterator.hasNext(); ) {
                TableInfo tableInfo = iterator.next();
                tableInfo.setReservations(null);
                iterator.remove(); //remove child relationship with tables
            }
            reservationRepository.deleteById(reservationId);
        }
    }

    private void reservationTimeValidation(Reservation reservation, List<Instant> reservationTimes) {
        for (Instant time : reservationTimes) {

            if (reservation.getTime().compareTo(time) == 0) {
                throw new BadRequestException(TABLE_RESERVATION_INVALID_TIME_SLOT);
            }

            if ((reservation.getTime().compareTo(time) > 0 && time.plus(1, ChronoUnit.HOURS)
                    .compareTo(reservation.getTime()) > 0) || (reservation.getTime().compareTo(time) < 0 && time.minus(
                    1, ChronoUnit.HOURS).compareTo(reservation.getTime()) < 0)) {
                throw new BadRequestException(TABLE_RESERVATION_INVALID_TIME_SLOT);
            }
        }
    }

    private void restaurantWorkingTimeValidation(Reservation reservation, WorkingTime workingTime,
            Restaurant currentRestaurant) {
        if (TimeConverterUtil.convertInstantToLocalTime(reservation.getTime()).compareTo(
                workingTime.getStartTime()) < 0 || TimeConverterUtil.convertInstantToLocalTime(reservation.getTime()).compareTo(
                workingTime.getEndTime()) > 0) {
            throw new BadRequestException(
                    "Restaurant is not working during the reservation time you are trying to create.");
        }

        LocalDate reservationDay = TimeConverterUtil.convertInstantToLocalDate(reservation.getTime());

        if (currentRestaurant.getNonWorkingDays().stream()
                .anyMatch(nonWorkingDay -> nonWorkingDay.getNonWorkingDayDate().equals(reservationDay))) {
            throw new BadRequestException(
                    "Restaurant is not working during that day you are trying to create reservation.");
        }
    }

    private WorkingTime workingTimeValidation(Reservation reservation, Restaurant currentRestaurant) {
        DayOfWeek reservationWeekDay = TimeConverterUtil.convertInstantToLocalDateTime(reservation.getTime()).getDayOfWeek();

        return currentRestaurant.getWorkingTimes().stream()
                .filter(workingTime1 -> workingTime1.getDayOfWeek().getDayOfWeek().equals(reservationWeekDay))
                .findFirst().orElseThrow(() -> new BadRequestException(
                        "Restaurant is not working during that day you are trying to create."));
    }
}
