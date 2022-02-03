package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.config.swagger.annotations.schedule.AddScheduleSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.schedule.FindScheduleSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.schedule.FreeSlotsByTennisCourtSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.schedule.FreeSlotsSwaggerInfo;
import com.tenniscourts.schedules.model.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.model.ScheduleDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/schedules")
@Api("Schedule resource, operations to manage schedules")
public class ScheduleController extends BaseRestController {

    private final ScheduleFacade scheduleFacade;

    @PostMapping
    @AddScheduleSwaggerInfo
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        var scheduleDTO = scheduleFacade.addSchedule(createScheduleRequestDTO);
        return ResponseEntity.created(locationByEntity(scheduleDTO.getId())).build();
    }

    @GetMapping("/{scheduleId}")
    @FindScheduleSwaggerInfo
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleFacade.findScheduleById(scheduleId));
    }

    @GetMapping("/freeslots")
    @FreeSlotsSwaggerInfo
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleFacade.findSchedulesByDates(
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @GetMapping("/freeslots/{tennisCourtId}")
    @FreeSlotsByTennisCourtSwaggerInfo
    public ResponseEntity<List<ScheduleDTO>> findFreeSchedulesByTennisCourtAndDates(
            @PathVariable Long tennisCourtId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleFacade.findFreeSchedulesByTennisCourtAndDates(tennisCourtId,
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }
}
