package com.tenniscourts.schedules;

import com.tenniscourts.schedules.model.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.model.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
//Added facade to avoid circular dependency with tennisCourtService
public class ScheduleFacade {

    private final ScheduleService scheduleService;
    private final TennisCourtService tennisCourtService;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        tennisCourtService.validateTennisCourt(createScheduleRequestDTO.getTennisCourtId());
        return scheduleService.addSchedule(createScheduleRequestDTO);
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleService.findSchedulesByDates(startDate, endDate);
    }

    public ScheduleDTO findScheduleById(Long scheduleId) {
        return scheduleService.findScheduleById(scheduleId);
    }

    public List<ScheduleDTO> findFreeSchedulesByTennisCourtAndDates(Long tennisCourtId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return scheduleService.findFreeSchedulesByTennisCourtAndDates(tennisCourtId, startDateTime, endDateTime);
    }
}
