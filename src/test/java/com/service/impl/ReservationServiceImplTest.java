package com.service.impl;

import com.domain.enums.EnumDayOfWeek;
import com.repository.ReservationRepository;
import com.repository.RestaurantRepository;
import com.repository.TableInfoRepository;
import com.repository.UserRepository;
import com.service.criteria.ReservationCriteria;
import com.service.mapper.*;
import com.service.specs.ReservationSpecificationService;
import com.service.dto.ReservationDto;
import com.web.errors.BadRequestException;
import com.web.errors.NotFoundException;
import com.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;
    private ReservationServiceImpl reservationService;
    private final ReservationCreateMapper reservationCreateMapper = new ReservationCreateMapperImpl();
    private final ReservationMapper reservationMapper = new ReservationMapperImpl();
    private final TableInfoMapper tableInfoMapper = new TableInfoMapperImpl();
    private final ReservationSpecificationService reservationSpecificationService = new ReservationSpecificationService();

    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private TableInfoRepository tableInfoRepository;
    private Reservation reservation;

    private static final String DEFAULT_RESERVATION_CUSTOMER_NAME = "John";
    private static final int DEFAULT_RESERVATION_PERSONS_COUNT = 5;
    private static final Instant DEFAULT_RESERVATION_TIME = Instant.ofEpochMilli(999999000);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(reservationCreateMapper, "tableInfoMapper", tableInfoMapper);
        ReflectionTestUtils.setField(reservationMapper, "tableInfoMapper", tableInfoMapper);

        reservation = new Reservation();

        reservation.setId(1L);
        reservation.setCustomerName(DEFAULT_RESERVATION_CUSTOMER_NAME);
        reservation.setPersons(DEFAULT_RESERVATION_PERSONS_COUNT);
        reservation.setTime(DEFAULT_RESERVATION_TIME);

        reservationService = new ReservationServiceImpl(reservationRepository, reservationCreateMapper,
                reservationMapper, restaurantRepository, tableInfoRepository,
                reservationSpecificationService, userRepository);
    }

    @Test
    void testFindAllWithCriteria() {
        // Arrange
        ReservationCriteria criteria = new ReservationCriteria();
        criteria.setTableInfoId(1L);
        criteria.setRestaurantId(1L);
        criteria.setUserId(1L);
        criteria.setTime(LocalDate.now());
        criteria.setCompleted(false);
        criteria.setCustomerName("Test");
        criteria.setPersons(4);

        List<Reservation> reservations = Collections.singletonList(reservation);

        when(reservationRepository.findAll(any(Specification.class))).thenReturn(reservations);

        // Act
        List<ReservationDto> result = reservationService.findAll(criteria);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get(0).getCustomerName()).isEqualTo(DEFAULT_RESERVATION_CUSTOMER_NAME);
        assertThat(result.get(0).getPersons()).isEqualTo(DEFAULT_RESERVATION_PERSONS_COUNT);

        // Verify that the repository was called with any specification
        verify(reservationRepository).findAll(any(Specification.class));
    }

    @Test
    void testCreateReservationMethod() {
        //given
        List<WorkingTime> workingTimes = createWorkingTimes();

        List<NonWorkingDay> nonWorkingDays = createNonWorkingDays();

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setNonWorkingDays(nonWorkingDays);

        User user = createUser();

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(tableInfoRepository.findById(1L)).thenReturn(Optional.of(tableInfo));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationDto expectedReservation = reservationService.create(reservationCreateMapper.toDto(reservation));

        //then
        assertThat(reservation.getCustomerName()).isEqualTo(expectedReservation.getCustomerName());
        assertThat(reservation.getTime()).isEqualTo(expectedReservation.getTime());
    }

    @Test
    void tableInfo_findById_withInvalidId_expectNotFoundException() {
        //given
        Restaurant restaurant = createNewRestaurant();

        User user = createUser();

        List<WorkingTime> workingTimes = createWorkingTimes();

        restaurant.setWorkingTimes(workingTimes);

        TableInfo tableInfo = createTableInfo();

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(tableInfoRepository.findById(tableInfo.getId())).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(NotFoundException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Table with the following id: " + tableInfo.getId() + " does not exist in the database.",
                exception.getMessage());
    }

    @Test
    void when_tryingToReserve_tables_inDifferentRestaurants_expectBadRequestException() {
        //given
        TableInfo tableInfo1 = createTableInfo();
        TableInfo tableInfo2 = createTableInfo();

        Restaurant restaurant1 = createNewRestaurant();
        restaurant1.setId(1L);
        Restaurant restaurant2 = createNewRestaurant();
        restaurant2.setId(2L);

        User user = createUser();

        List<WorkingTime> workingTimes = createWorkingTimes();

        restaurant1.setWorkingTimes(workingTimes);
        restaurant2.setWorkingTimes(workingTimes);

        tableInfo1.setRestaurant(restaurant1);
        tableInfo2.setRestaurant(restaurant2);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo1);
        tableInfos.add(tableInfo2);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(any())).thenReturn(Optional.of(restaurant1));
        when(tableInfoRepository.findById(any())).thenReturn(Optional.of(tableInfo1));

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant2);

        //then
        Exception exception = assertThrows(BadRequestException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("The tables you are trying to reserve are not in the same restaurant.", exception.getMessage());
    }

    @Test
    void when_tryingToReserve_inRestaurant_whichDoesNotExist_expectBadRequestException() {
        //given
        TableInfo tableInfo = createTableInfo();
        Restaurant restaurant = createNewRestaurant();

        tableInfo.setRestaurant(restaurant);

        User user = createUser();

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(tableInfo.getId())).thenReturn(Optional.empty());

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //then
        Exception exception = assertThrows(NotFoundException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Restaurant with the following id: " + reservation.getRestaurant()
                .getId() + " does not exist in the database.", exception.getMessage());
    }

    @Test
    void when_trying_toReserve_during_restaurant_nonWorkingDayInDB_expectBadRequestException() {
        //given
        reservation.setTime(reservation.getTime().minus(11, ChronoUnit.DAYS));

        List<NonWorkingDay> nonWorkingDays = createNonWorkingDays();

        List<WorkingTime> workingTimes = createWorkingTimes();

        User user = createUser();

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setNonWorkingDays(nonWorkingDays);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(tableInfo.getId())).thenReturn(Optional.of(restaurant));

        //then
        Exception exception = assertThrows(BadRequestException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Restaurant is not working during that day you are trying to create.", exception.getMessage());
    }

    @Test
    void when_trying_toReserve_during_nonWorkingDay_expectBadRequestException() {
        //given
        reservation.setTime(reservation.getTime().plus(1, ChronoUnit.DAYS));

        List<WorkingTime> workingTimes = createWorkingTimes();

        User user = createUser();

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(tableInfo.getId())).thenReturn(Optional.of(restaurant));

        //then
        Exception exception = assertThrows(BadRequestException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Restaurant is not working during that day you are trying to create.", exception.getMessage());
    }

    @Test
    void when_tryingToReserve_inRestaurant_duringTimeWhich_theRestaurantIsClosed_expectBadRequestException() {
        //given
        reservation.setTime(reservation.getTime().plus(10, ChronoUnit.HOURS));

        List<WorkingTime> workingTimes = createWorkingTimes();

        User user = createUser();

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(tableInfo.getId())).thenReturn(Optional.of(restaurant));

        //then
        Exception exception = assertThrows(BadRequestException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Restaurant is not working during the reservation time you are trying to create.",
                exception.getMessage());
    }

    @Test
    void when_tryingToReserve_withATime_whichThereIsAlready_reservationCreated() {
        //given
        List<WorkingTime> workingTimes = createWorkingTimes();

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        User user = createUser();

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setReservations(reservations);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(tableInfoRepository.findById(any())).thenReturn(Optional.of(tableInfo));
        when(restaurantRepository.findById(tableInfo.getId())).thenReturn(Optional.of(restaurant));

        //then
        Exception exception = assertThrows(BadRequestException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Table cannot be reserved, because the time slot is not free.", exception.getMessage());
    }

    @Test
    void when_tryingToReserve_withATime_lessThan1Hour_beforeSomeExistingReservation() {
        //given
        Reservation newReservation = new Reservation();

        newReservation.setId(2L);
        newReservation.setCustomerName("Jack");
        newReservation.setPersons(4);
        newReservation.setTime(reservation.getTime().minus(45, ChronoUnit.MINUTES));

        List<WorkingTime> workingTimes = createWorkingTimes();

        User user = createUser();

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(newReservation);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setReservations(reservations);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(tableInfoRepository.findById(any())).thenReturn(Optional.of(tableInfo));
        when(restaurantRepository.findById(tableInfo.getId())).thenReturn(Optional.of(restaurant));

        //then
        Exception exception = assertThrows(BadRequestException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Table cannot be reserved, because the time slot is not free.", exception.getMessage());
    }

    @Test
    void when_tryingToReserve_withATime_lessThan1Hour_afterSomeExistingReservation() {
        //given
        Reservation newReservation = new Reservation();

        newReservation.setId(2L);
        newReservation.setCustomerName("Jack");
        newReservation.setPersons(4);
        newReservation.setTime(reservation.getTime().plus(45, ChronoUnit.MINUTES));

        List<WorkingTime> workingTimes = createWorkingTimes();

        User user = createUser();

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(newReservation);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setReservations(reservations);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setUser(user);
        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(tableInfoRepository.findById(any())).thenReturn(Optional.of(tableInfo));
        when(restaurantRepository.findById(tableInfo.getId())).thenReturn(Optional.of(restaurant));

        //then
        Exception exception = assertThrows(BadRequestException.class,
                () -> reservationService.create(reservationCreateMapper.toDto(reservation)));
        assertEquals("Table cannot be reserved, because the time slot is not free.", exception.getMessage());
    }

    @Test
    void find_reservation_byId() {
        //when
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        ReservationDto expectedReservation = reservationService.findById(reservation.getId());

        //then
        assertThat(expectedReservation).isNotNull();
    }

    @Test
    void updateRestaurant() {
        //when
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        reservation.setCustomerName("Nick");

        ReservationDto expectedReservation = reservationService.updateReservation(reservationMapper.toDto(reservation));

        //then
        assertThat(expectedReservation.getCustomerName()).isEqualTo("Nick");
    }

    @Test
    void delete() {
        //when
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        reservationService.delete(reservation.getId());

        //then
        verify(reservationRepository, times(1)).deleteById(reservation.getId());
    }

    @Test()
    void findById_withInvalidId_expectNotFoundException() {
        //given
        reservation.setId(null);

        //when
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(NotFoundException.class,
                () -> reservationService.findById(reservation.getId()));
        assertEquals("Reservation with the following id: " + reservation.getId() + " does not exists in the database.",
                exception.getMessage());
    }

    @Test()
    void update_withInvalidId_expectNotFoundException() {
        //given
        reservation.setId(null);

        //when
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(NotFoundException.class,
                () -> reservationService.updateReservation(reservationMapper.toDto(reservation)));
        assertEquals("Reservation with the following id: " + reservation.getId() + " does not exists in the database.",
                exception.getMessage());
    }

    @Test()
    void delete_withInvalidId_expectNotFoundException() {
        //given
        reservation.setId(null);

        //then
        Exception exception = assertThrows(NotFoundException.class,
                () -> reservationService.delete(reservation.getId()));
        assertEquals("Reservation with the following id: " + reservation.getId() + " does not exists in the database.",
                exception.getMessage());
    }

    @Test
    void testDeleteReservationAndClearTableInfos() {
        // Arrange
        Long reservationId = 1L;

        // Creating a Reservation with associated TableInfos
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        TableInfo tableInfo1 = new TableInfo();
        tableInfo1.setId(1L);
        TableInfo tableInfo2 = new TableInfo();
        tableInfo2.setId(2L);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo1);
        tableInfos.add(tableInfo2);
        reservation.setTableInfos(tableInfos);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        reservationService.delete(reservationId);

        // Assert
        assertThat(tableInfo1.getReservations()).isNull();
        assertThat(tableInfo2.getReservations()).isNull();
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).deleteById(reservationId);
    }

    public LocalDateTime convertInstantToLocalDateTime(Instant time) {
        return LocalDateTime.ofInstant(time, ZoneOffset.systemDefault());
    }

    private TableInfo createTableInfo() {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setId(1L);
        tableInfo.setDescription("Test");
        tableInfo.setPersonCapacity(4);
        tableInfo.setTableNumber(5);
        return tableInfo;
    }

    private Restaurant createNewRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test name");
        restaurant.setRating(4.4f);
        restaurant.setDescription("Test description");
        return restaurant;
    }

    private List<WorkingTime> createWorkingTimes() {
        WorkingTime workingTime = new WorkingTime();
        workingTime.setStartTime(LocalTime.of(9, 0));
        workingTime.setEndTime(LocalTime.of(21, 0));
        workingTime.setDayOfWeek(EnumDayOfWeek.MONDAY);
        List<WorkingTime> workingTimes = new ArrayList<>();
        workingTimes.add(workingTime);
        return workingTimes;
    }

    private List<NonWorkingDay> createNonWorkingDays() {
        NonWorkingDay nonWorkingDay = new NonWorkingDay();
        nonWorkingDay.setNonWorkingDayDate(LocalDate.of(1970, 1, 1));
        nonWorkingDay.setDescription("New Year's Day");
        List<NonWorkingDay> nonWorkingDays = new ArrayList<>();
        nonWorkingDays.add(nonWorkingDay);
        return nonWorkingDays;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test123");
        return user;
    }
}