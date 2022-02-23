package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("reservations")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(
            @PathVariable("reservationId") Long reservationId, @RequestParam("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
