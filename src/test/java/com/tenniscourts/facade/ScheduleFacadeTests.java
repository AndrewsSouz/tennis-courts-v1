package com.tenniscourts.facade;

import com.tenniscourts.schedules.ScheduleFacade;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.tenniscourts.TennisCourtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.tenniscourts.stubs.ScheduleStubs.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleFacadeTests {

    @Mock
    ScheduleService scheduleService;

    @Mock
    TennisCourtService tennisCourtService;

    @InjectMocks
    ScheduleFacade scheduleFacade;


    @Test
    void addScheduleShouldReturnTheSchedule() {
        var createScheduleDtoStub = createScheduleRequestDTOStub();
        var scheduleDtoStub = scheduleDTOStub();

        when(scheduleService.addSchedule(createScheduleDtoStub)).thenReturn(scheduleDtoStub);

        var actualStub = scheduleFacade.addSchedule(createScheduleDtoStub);

        assertEquals(scheduleDtoStub, actualStub);
    }

    @Test
    void findSchedulesByDatesShouldReturnTheSchedules() {
        var scheduleDtoStub = scheduleDTOStub();

        var startDateTime = LocalDateTime.of(2021, 8, 8, 8, 0);
        var endDateTime = LocalDateTime.of(2021, 8, 8, 23, 0);

        when(scheduleService.findSchedulesByDates(startDateTime, endDateTime)).thenReturn(List.of(scheduleDtoStub));

        var actualStub = scheduleFacade.findSchedulesByDates(startDateTime, endDateTime);

        assertEquals(List.of(scheduleDtoStub), actualStub);
    }

    @Test
    void findScheduleByIdShouldReturnTheSchedule() {
        var scheduleDtoStub = scheduleDTOStub();
        var scheduleId = 1L;

        when(scheduleService.findScheduleById(scheduleId)).thenReturn(scheduleDtoStub);

        var actualStub = scheduleFacade.findScheduleById(scheduleId);

        assertEquals(scheduleDtoStub, actualStub);
    }

    @Test
    void findSchedulesByTennisCourtAndDatesShouldReturnTheSchedules() {
        var scheduleDtoStub = scheduleDTOStub();

        var tennisCourtId = 1L;
        var startDateTime = LocalDateTime.of(2021, 8, 8, 8, 0);
        var endDateTime = LocalDateTime.of(2021, 8, 8, 23, 0);

        when(scheduleService.findFreeSchedulesByTennisCourtAndDates(tennisCourtId, startDateTime, endDateTime)).thenReturn(List.of(scheduleDtoStub));

        var actualStub = scheduleFacade.findFreeSchedulesByTennisCourtAndDates(tennisCourtId, startDateTime, endDateTime);

        assertEquals(List.of(scheduleDtoStub), actualStub);
    }

}
