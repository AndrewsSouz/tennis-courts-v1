package com.tenniscourts.reservations;

import com.tenniscourts.reservations.model.Reservation;
import com.tenniscourts.reservations.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsBySchedule_Id(Long scheduleId);

    List<Reservation> findBySchedule_Id(Long scheduleId);

    List<Reservation> findByReservationStatusAndSchedule_StartDateTimeGreaterThanEqualAndSchedule_EndDateTimeLessThanEqual(ReservationStatus reservationStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

//    List<Reservation> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqualAndTennisCourt(LocalDateTime startDateTime, LocalDateTime endDateTime, TennisCourt tennisCourt);
}
