package com.web;

import com.domain.*;
import com.domain.enums.EnumDayOfWeek;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.repository.*;
import com.service.dto.ReservationCreateDto;
import com.service.dto.ReservationDto;
import com.service.mapper.ReservationCreateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TableInfoRepository tableInfoRepository;

    @Autowired
    private WorkingTimeRepository workingTimeRepository;

    @Autowired
    private NonWorkingDayRepository nonWorkingDayRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationCreateMapper reservationCreateMapper;

    private Reservation reservation;

    private static final String DEFAULT_RESERVATION_CUSTOMER_NAME = "John";
    private static final String DEFAULT_RESERVATION_CUSTOMER_PHONE = "+359777777777";
    private static final int DEFAULT_RESERVATION_PERSONS_COUNT = 5;
    private static final Instant DEFAULT_RESERVATION_TIME = Instant.ofEpochMilli(999999000);

    @BeforeEach
    void setUp() {
        reservation = new Reservation();

        reservation.setCustomerName(DEFAULT_RESERVATION_CUSTOMER_NAME);
        reservation.setCustomerPhone(DEFAULT_RESERVATION_CUSTOMER_PHONE);
        reservation.setPersons(DEFAULT_RESERVATION_PERSONS_COUNT);
        reservation.setTime(DEFAULT_RESERVATION_TIME);

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void shouldCreateReservation() throws Exception {
        //given
        List<WorkingTime> workingTimes = createWorkingTimes();
        List<NonWorkingDay> nonWorkingDays = createNonWorkingDays();

        workingTimeRepository.saveAll(workingTimes);
        nonWorkingDayRepository.saveAll(nonWorkingDays);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setNonWorkingDays(nonWorkingDays);

        for (WorkingTime workingTime : workingTimes) {
            workingTime.setRestaurant(restaurant);
        }

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        restaurantRepository.save(restaurant);
        tableInfoRepository.save(tableInfo);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(reservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerName", is("John")));
    }

    @Test
    void tableInfo_findById_withInvalidId_expectNotFoundException() throws Exception {
        //given
        Restaurant restaurant = createNewRestaurant();

        TableInfo tableInfo = createTableInfo();
        tableInfo.setId(12L);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(reservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void when_tryingToReserve_tables_inDifferentRestaurants_expectBadRequestException() throws Exception {
        //given
        TableInfo tableInfo1 = createTableInfo();
        TableInfo tableInfo2 = createTableInfo();

        Restaurant restaurant1 = createNewRestaurant();
        restaurant1.setId(1L);
        Restaurant restaurant2 = createNewRestaurant();
        restaurant2.setId(2L);

        tableInfo1.setRestaurant(restaurant2);
        tableInfo2.setRestaurant(restaurant1);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo1);
        tableInfos.add(tableInfo2);

        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant2);

        //when
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);
        tableInfoRepository.save(tableInfo1);
        tableInfoRepository.save(tableInfo2);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(reservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void when_tryingToReserve_inRestaurant_duringNonWorkingDay_expectRuntimeException() throws Exception {
        //given
        Instant currentReservationTime = reservation.getTime();

        reservation.setTime(currentReservationTime.plus(24, ChronoUnit.HOURS));

        List<WorkingTime> workingTimes = createWorkingTimes();

        workingTimeRepository.saveAll(workingTimes);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);

        for (WorkingTime workingTime : workingTimes) {
            workingTime.setRestaurant(restaurant);
        }

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        restaurantRepository.save(restaurant);
        tableInfoRepository.save(tableInfo);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(reservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void when_trying_toReserve_during_restaurant_nonWorkingDayInDB_expectBadRequestException() throws Exception {
        //given
        reservation.setTime(reservation.getTime().minus(11, ChronoUnit.DAYS));

        List<WorkingTime> workingTimes = createWorkingTimes();
        List<NonWorkingDay> nonWorkingDays = createNonWorkingDays();

        nonWorkingDayRepository.saveAll(nonWorkingDays);
        workingTimeRepository.saveAll(workingTimes);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setNonWorkingDays(nonWorkingDays);


        for (WorkingTime workingTime : workingTimes) {
            workingTime.setRestaurant(restaurant);
        }

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        restaurantRepository.save(restaurant);
        tableInfoRepository.save(tableInfo);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(reservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void when_tryingToReserve_inRestaurant_duringTimeWhich_theRestaurantIsClosed_expectRuntimeException() throws Exception {
        //given
        Instant currentReservationTime = reservation.getTime();

        reservation.setTime(currentReservationTime.plus(12, ChronoUnit.HOURS));

        List<WorkingTime> workingTimes = createWorkingTimes();
        List<NonWorkingDay> nonWorkingDays = createNonWorkingDays();

        workingTimeRepository.saveAll(workingTimes);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setNonWorkingDays(nonWorkingDays);

        for (WorkingTime workingTime : workingTimes) {
            workingTime.setRestaurant(restaurant);
        }

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        reservation.setTableInfos(tableInfos);
        reservation.setRestaurant(restaurant);

        //when
        restaurantRepository.save(restaurant);
        tableInfoRepository.save(tableInfo);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(reservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void when_tryingToReserve_withTheSameTime_whichThereIsAlready_reservationCreated() throws Exception {
        //given
        Reservation newReservation = new Reservation();

        newReservation.setId(2L);
        newReservation.setCustomerName("Jack");
        newReservation.setPersons(4);
        newReservation.setTime(Instant.ofEpochMilli(999999000));

        List<WorkingTime> workingTimes = createWorkingTimes();
        workingTimeRepository.saveAll(workingTimes);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setReservations(reservations);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        newReservation.setTableInfos(tableInfos);
        newReservation.setRestaurant(restaurant);

        //when
        reservationRepository.save(reservation);
        restaurantRepository.save(restaurant);
        tableInfoRepository.save(tableInfo);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(newReservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void when_tryingToReserve_withATime_lessThan1Hour_beforeSomeExistingReservation() throws Exception {
        //given
        Reservation newReservation = new Reservation();

        newReservation.setId(2L);
        newReservation.setCustomerName("Jack");
        newReservation.setPersons(4);
        newReservation.setTime(reservation.getTime().minus(45, ChronoUnit.MINUTES));

        List<WorkingTime> workingTimes = createWorkingTimes();
        workingTimeRepository.saveAll(workingTimes);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setReservations(reservations);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        newReservation.setTableInfos(tableInfos);
        newReservation.setRestaurant(restaurant);

        //when
        reservationRepository.save(reservation);
        restaurantRepository.save(restaurant);
        tableInfoRepository.save(tableInfo);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(newReservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void when_tryingToReserve_withATime_lessThan1Hour_afterSomeExistingReservation() throws Exception {
        //given
        Reservation newReservation = new Reservation();

        newReservation.setId(2L);
        newReservation.setCustomerName("Jack");
        newReservation.setPersons(4);
        newReservation.setTime(reservation.getTime().plus(45, ChronoUnit.MINUTES));

        List<WorkingTime> workingTimes = createWorkingTimes();
        workingTimeRepository.saveAll(workingTimes);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        Restaurant restaurant = createNewRestaurant();
        restaurant.setWorkingTimes(workingTimes);
        restaurant.setReservations(reservations);

        TableInfo tableInfo = createTableInfo();
        tableInfo.setRestaurant(restaurant);

        List<TableInfo> tableInfos = new ArrayList<>();
        tableInfos.add(tableInfo);

        newReservation.setTableInfos(tableInfos);
        newReservation.setRestaurant(restaurant);

        //when
        reservationRepository.save(reservation);
        restaurantRepository.save(restaurant);
        tableInfoRepository.save(tableInfo);

        ReservationCreateDto reservationCreateDto = reservationCreateMapper.toDto(newReservation);

        ResultActions response = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(reservationCreateDto)));

        //then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void getAllReservations() throws Exception {
        //given
        reservation.setId(1L);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        //when
        reservationRepository.saveAll(reservations);

        ResultActions response = mockMvc.perform(get("/api/reservation"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()"
                        , is(reservations.size())));
    }

    @Test
    void getReservationById() throws Exception {
        reservation.setId(1L);
        reservationRepository.save(reservation);

        ResultActions response = mockMvc.perform(get("/api/reservation/{reservationId}", reservation.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerName", is("John")));
    }

    @Test
    void updateReservation() throws Exception {
        //when
        reservationRepository.save(reservation);

        ReservationDto updatedReservation = new ReservationDto();
        updatedReservation.setId(1L);
        updatedReservation.setCustomerName("Nick");
        updatedReservation.setCustomerPhone("3598888888");
        updatedReservation.setTime(Instant.now());
        updatedReservation.setRestaurantId(1L);

        ResultActions response = mockMvc.perform(put("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedReservation)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.customerName", is(updatedReservation.getCustomerName())));
    }

    @Test
    void deleteReservation() throws Exception {
        //when
        reservationRepository.save(reservation);

        ResultActions response = mockMvc.perform(delete("/api/reservation/{reservationId}", reservation.getId()));

        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void delete_reservationWithInvalidId_expectNotFoundException() throws Exception {
        //when
        reservation.setId(11L);

        ResultActions response = mockMvc.perform(delete("/api/reservation/{reservationId}", reservation.getId()));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    private TableInfo createTableInfo() {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setId(1L);
        tableInfo.setDescription("Test");
        tableInfo.setPersonCapacity(4);
        tableInfo.setPersonCapacity(5);
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
        WorkingTime workingTime1 = new WorkingTime();
        workingTime1.setStartTime(LocalTime.of(9, 0));
        workingTime1.setEndTime(LocalTime.of(21, 0));
        workingTime1.setDayOfWeek(EnumDayOfWeek.MONDAY);

        WorkingTime workingTime2 = new WorkingTime();
        workingTime2.setStartTime(LocalTime.of(9, 0));
        workingTime2.setEndTime(LocalTime.of(21, 0));
        workingTime2.setDayOfWeek(EnumDayOfWeek.THURSDAY);

        List<WorkingTime> workingTimes = new ArrayList<>();
        workingTimes.add(workingTime1);
        workingTimes.add(workingTime2);
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
}