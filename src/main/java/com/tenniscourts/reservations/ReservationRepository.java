package com.tenniscourts.reservations;

import com.tenniscourts.reservations.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByScheduleId(Long scheduleId);

    List<Reservation> findByScheduleStartDateTimeLessThanEqual(LocalDateTime startDateTime);
}
