package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.model.CreateReservationRequestDTO;
import com.tenniscourts.reservations.model.Reservation;
import com.tenniscourts.reservations.model.ReservationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.tenniscourts.reservations.model.ReservationStatus.*;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        if (existsReservationByScheduleId(createReservationRequestDTO.getScheduleId())) {
            throw new AlreadyExistsEntityException("Already exists a reservation for this time");
        }
        var reservation = reservationMapper.map(createReservationRequestDTO);
        reservation.setValue(new BigDecimal(10));
        reservation.setReservationStatus(READY_TO_PLAY);
        return reservationMapper.map(reservationRepository.save(reservation));
    }

    public ReservationDTO findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(this::validateCancellationAndUpdateReservation)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found."));
    }

    private Reservation validateCancellationAndUpdateReservation(Reservation reservation) {
        validateCancellation(reservation);
        return updateReservation(reservation, getRefundValue(reservation));
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue) {
        reservation.setReservationStatus(CANCELLED);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours > 24) {
            return reservation.getValue();
        } else if (hours <= 23.59 && hours >= 12) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.75));
        } else if (hours <= 11.59 && hours >= 2) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.5));
        } else if (hours <= 1.59 && hours > 0.01) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.25));
        }

        return BigDecimal.ZERO;
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        ReservationDTO previousReservationToCheck = findReservationById(previousReservationId);

        if (scheduleId.equals(previousReservationToCheck.getSchedule().getId())) {
            return previousReservationToCheck;
        }

        var previousReservation = cancel(previousReservationId);

        previousReservation.setReservationStatus(RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation =
                bookReservation(new CreateReservationRequestDTO(previousReservation.getGuest().getId(), scheduleId));
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    public boolean existsReservationByScheduleId(Long scheduleId) {
        return reservationRepository.existsByScheduleId(scheduleId);
    }

}
