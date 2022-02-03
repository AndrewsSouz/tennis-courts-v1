package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.config.swagger.annotations.reservation.*;
import com.tenniscourts.reservations.model.CreateReservationRequestDTO;
import com.tenniscourts.reservations.model.ReservationDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservations")
@Api("Reservation resource, operations to manage reservations")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    @BookReservationSwaggerInfo
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{reservationId}")
    @FindReservationSwaggerInfo
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservationById(reservationId));
    }

    @PatchMapping("/cancel/{reservationId}")
    @CancelReservationSwaggerInfo
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PatchMapping("/reschedule")
    @RescheduleReservationSwaggerInfo
    public ResponseEntity<ReservationDTO> rescheduleReservation(@RequestParam Long reservationId,
                                                                @RequestParam Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }

    @GetMapping("/history")
    @ReservationHistoricSwaggerInfo
    public ResponseEntity<List<ReservationDTO>> reservationHistoric() {
        return ResponseEntity.ok(reservationService.findPastReservations());
    }
}
