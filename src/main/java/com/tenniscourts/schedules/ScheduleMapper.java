package com.tenniscourts.schedules;

import com.tenniscourts.schedules.model.Schedule;
import com.tenniscourts.schedules.model.ScheduleDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    Schedule map(ScheduleDTO source);

    ScheduleDTO map(Schedule source);

    List<ScheduleDTO> map(List<Schedule> source);
}
