package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.times;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    private ReservationRepository repository;

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void refundValueShouldReturnZeroWhenEnoughTimeHasntPassed() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now();

        schedule.setStartDateTime(startDateTime);
        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), BigDecimal.ZERO);
    }

    @Test
    public void cancelShouldThrowEntityNotFoundWhenInvalidId() {
        Long nonExistingId = 1L;
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(nonExistingId);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            reservationService.cancelReservation(nonExistingId);
        });
    }

    @Test
    public void findShouldThrowWhenInvalidId() {
        Long nonExistingId = 1L;
        Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(nonExistingId);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            reservationService.findReservation(nonExistingId);
        });
    }
}