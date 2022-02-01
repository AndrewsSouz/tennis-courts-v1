package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.config.swagger.annotations.reservation.BookReservationSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.reservation.CancelReservationSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.reservation.FindReservationSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.reservation.RescheduleReservationSwaggerInfo;
import com.tenniscourts.reservations.model.CreateReservationRequestDTO;
import com.tenniscourts.reservations.model.ReservationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation("Return the all the reservations before the actual date")
    @ApiResponses(@ApiResponse(code = 200, message = "Successfully retrieved the reservation history"))
    public ResponseEntity<List<ReservationDTO>> reservationHistoric() {
        return ResponseEntity.ok(reservationService.findPastReservations());
    }
}
