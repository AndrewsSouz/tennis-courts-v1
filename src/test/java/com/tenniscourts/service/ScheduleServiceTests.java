package com.tenniscourts.service;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.ReservationService;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleRepository;
import com.tenniscourts.schedules.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.tenniscourts.stubs.ScheduleStubs.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTests {

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ScheduleMapper scheduleMapper;

    @Mock
    ReservationService reservationService;

    @InjectMocks
    ScheduleService scheduleService;


    @Test
    void addScheduleShouldReturnTheSchedule() {
        var createScheduleDtoStub = createScheduleRequestDTOStub();
        var scheduleDtoStub = scheduleDTOStub();
        var scheduleStub = scheduleStub();

        when(scheduleRepository.save(scheduleStub)).thenReturn(scheduleStub);
        when(scheduleMapper.map(createScheduleDtoStub)).thenReturn(scheduleStub);
        when(scheduleMapper.map(scheduleStub)).thenReturn(scheduleDtoStub);

        var actualStub = scheduleService.addSchedule(createScheduleDtoStub);

        assertEquals(scheduleDtoStub, actualStub);
    }

    @Test
    void addScheduleShouldThrowException() {
        var createScheduleDtoStub = createScheduleRequestDTOStub();

        when(scheduleRepository.existsByTennisCourtIdAndStartDateTime(createScheduleDtoStub.getTennisCourtId(), createScheduleDtoStub.getStartDateTime()))
                .thenReturn(true);

        assertThrows(AlreadyExistsEntityException.class, () -> scheduleService.addSchedule(createScheduleDtoStub));
    }

    @Test
    void findSchedulesByDatesShouldReturnAListOfSchedules() {
        var scheduleDtoStub = scheduleDTOStub();
        scheduleDtoStub.setTennisCourtId(1L);
        var scheduleStub = scheduleStub();
        scheduleStub.getTennisCourt().setId(1L);

        var startDateTime = LocalDateTime.of(2021, 8, 8, 8, 0);
        var endDateTime = LocalDateTime.of(2021, 8, 8, 23, 0);

        when(scheduleRepository.findSchedulesByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDateTime, endDateTime))
                .thenReturn(List.of(scheduleStub));
        when(reservationService.existsReservationByScheduleId(scheduleStub.getId())).thenReturn(false);
        when(scheduleMapper.map(scheduleStub)).thenReturn(scheduleDtoStub);

        var actualStub = scheduleService.findSchedulesByDates(startDateTime, endDateTime);

        assertEquals(List.of(scheduleDtoStub), actualStub);
    }

    @Test
    void findScheduleByIdShouldReturnASchedule() {
        var scheduleDtoStub = scheduleDTOStub();
        var scheduleStub = scheduleStub();

        var scheduleId = 1L;

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleStub));
        when(scheduleMapper.map(scheduleStub)).thenReturn(scheduleDtoStub);

        var actualStub = scheduleService.findScheduleById(scheduleId);

        assertEquals(scheduleDtoStub, actualStub);
    }

    @Test
    void findScheduleByIdShouldThrowAnException() {
        var scheduleId = 1L;

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> scheduleService.findScheduleById(scheduleId));
    }

    @Test
    void findSchedulesByTennisCourtIdShouldReturnAListOfSchedules() {
        var scheduleDtoStub = scheduleDTOStub();
        var scheduleStub = scheduleStub();

        var tennisCourtId = 1L;

        when(scheduleRepository.findByTennisCourtIdOrderByStartDateTime(tennisCourtId)).thenReturn(List.of(scheduleStub));
        when(scheduleMapper.map(scheduleStub)).thenReturn(scheduleDtoStub);

        var actualStub = scheduleService.findSchedulesByTennisCourtId(tennisCourtId);

        assertEquals(List.of(scheduleDtoStub), actualStub);
    }

    @Test
    void findFreeTimeSlotsShouldReturnAListOfSchedules() {
        var scheduleDtoStub = scheduleDTOStub();
        var scheduleStub = scheduleStub();

        var tennisCourtId = 1L;
        var startDateTime = LocalDateTime.of(2021, 8, 8, 8, 0);
        var endDateTime = LocalDateTime.of(2021, 8, 8, 23, 0);

        when(scheduleRepository.findSchedulesByTennisCourtIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                tennisCourtId, startDateTime, endDateTime))
                .thenReturn(List.of(scheduleStub));
        when(reservationService.existsReservationByScheduleId(scheduleStub.getId())).thenReturn(false);
        when(scheduleMapper.map(scheduleStub)).thenReturn(scheduleDtoStub);

        var actualStub = scheduleService.findFreeSchedulesByTennisCourtAndDates(tennisCourtId, startDateTime, endDateTime);

        assertEquals(List.of(scheduleDtoStub), actualStub);
    }

    @Test
    void findFreeTimeSlotsShouldNotReturnTheReservedSchedule() {
        var scheduleDtoStub = scheduleDTOStub();
        var scheduleStub = scheduleStub();

        var tennisCourtId = 1L;
        var startDateTime = LocalDateTime.of(2021, 8, 8, 8, 0);
        var endDateTime = LocalDateTime.of(2021, 8, 8, 23, 0);

        when(scheduleRepository.findSchedulesByTennisCourtIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                tennisCourtId, startDateTime, endDateTime))
                .thenReturn(List.of(scheduleStub));
        when(reservationService.existsReservationByScheduleId(scheduleStub.getId())).thenReturn(true);

        var actualStub = scheduleService.findFreeSchedulesByTennisCourtAndDates(tennisCourtId, startDateTime, endDateTime);

        assertEquals(Collections.emptyList(), actualStub);
    }
}
