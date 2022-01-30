package com.tenniscourts.schedules;

import com.tenniscourts.schedules.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);
}