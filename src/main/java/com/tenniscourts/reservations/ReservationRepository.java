package com.tenniscourts.reservations;

import com.tenniscourts.reservations.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByScheduleId(Long scheduleId);
}
