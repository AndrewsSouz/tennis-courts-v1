package com.tenniscourts.controller.reservations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.reservations.ReservationController;
import com.tenniscourts.reservations.ReservationService;
import com.tenniscourts.reservations.model.ReservationDTO;
import com.tenniscourts.schedules.model.ScheduleDTO;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.tenniscourts.stubs.ReservationStubs.createReservationRequestDTOStub;
import static com.tenniscourts.stubs.ReservationStubs.reservationDTOStub;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {ReservationController.class, ReservationService.class})
class ReservationControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @Test
    void bookReservationShouldReturnStatusCREATED() throws Exception {
        var createReservationDtoStub = createReservationRequestDTOStub();
        var reservationDtoStub = reservationDTOStub();

        var json = new ObjectMapper().writeValueAsString(createReservationDtoStub);

        when(reservationService.bookReservation(createReservationDtoStub)).thenReturn(reservationDtoStub);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findReservationByIdShouldReturnStatusOKAndTheReservation() throws Exception {
        var reservatioDtoStub = reservationDTOStub();

        when(reservationService.findReservationById(1L)).thenReturn(reservatioDtoStub);

        this.mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("reservationStatus", Matchers.is("READY_TO_PLAY")))
                .andExpect(jsonPath("schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("value", Matchers.is(10)))
                .andExpect(jsonPath("guestId", Matchers.is(1)));
    }

    @Test
    void cancelReservationShouldReturnStatusOKAndTheReservation() throws Exception {
        var reservatioDtoStub = reservationDTOStub();
        reservatioDtoStub.setReservationStatus("CANCELLED");
        reservatioDtoStub.setRefundValue(new BigDecimal(5));
        reservatioDtoStub.setValue(new BigDecimal(5));

        when(reservationService.cancelReservation(1L)).thenReturn(reservatioDtoStub);

        this.mockMvc.perform(patch("/reservations/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("reservationStatus", Matchers.is("CANCELLED")))
                .andExpect(jsonPath("schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("refundValue", Matchers.is(5)))
                .andExpect(jsonPath("value", Matchers.is(5)))
                .andExpect(jsonPath("guestId", Matchers.is(1)));
    }

    @Test
    void rescheduleReservationShouldReturnStatusOKAndTheReservation() throws Exception {
        var reservatioDtoStub = reservationDTOStub();
        reservatioDtoStub.setId(2L);
        reservatioDtoStub.setScheduledId(2L);
        reservatioDtoStub.getSchedule().setId(2L);
        reservatioDtoStub.setPreviousReservation(
                ReservationDTO.builder()
                        .id(1L)
                        .schedule(ScheduleDTO.builder()
                                .id(1L)
                                .tennisCourt(TennisCourtDTO.builder()
                                        .id(1L)
                                        .name("Test Court")
                                        .build())
                                .startDateTime(LocalDateTime.of(2021, 8, 8, 8, 0))
                                .endDateTime(LocalDateTime.of(2021, 8, 8, 9, 0))
                                .build())
                        .reservationStatus("RESCHEDULED")
                        .refundValue(new BigDecimal(10))
                        .value(new BigDecimal(0))
                        .scheduledId(1L)
                        .guestId(1L)
                        .build());

        when(reservationService.rescheduleReservation(1L, 2L)).thenReturn(reservatioDtoStub);

        this.mockMvc.perform(patch("/reservations/reschedule")
                        .param("reservationId", "1")
                        .param("scheduleId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(2)))
                .andExpect(jsonPath("reservationStatus", Matchers.is("READY_TO_PLAY")))

                .andExpect(jsonPath("previousReservation.id", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("previousReservation.schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("previousReservation.schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("previousReservation.reservationStatus", Matchers.is("RESCHEDULED")))
                .andExpect(jsonPath("previousReservation.refundValue", Matchers.is(10)))
                .andExpect(jsonPath("previousReservation.value", Matchers.is(0)))
                .andExpect(jsonPath("previousReservation.scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.guestId", Matchers.is(1)))

                .andExpect(jsonPath("schedule.id", Matchers.is(2)))
                .andExpect(jsonPath("schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("schedule.endDateTime", Matchers.is("2021-08-08T09:00")))

                .andExpect(jsonPath("schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.tennisCourt.name", Matchers.is("Test Court")))

                .andExpect(jsonPath("scheduledId", Matchers.is(2)))
                .andExpect(jsonPath("value", Matchers.is(10)))
                .andExpect(jsonPath("guestId", Matchers.is(1)));
    }

    @Test
    void reservationHistoricShouldReturnStatusOKAndAListOfReservations() throws Exception {
        var reservationDtoStub = reservationDTOStub();

        when(reservationService.findPastReservations()).thenReturn(List.of(reservationDtoStub));

        this.mockMvc.perform(get("/reservations/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].reservationStatus", Matchers.is("READY_TO_PLAY")))
                .andExpect(jsonPath("$[0].schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("$[0].schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("$[0].schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("$[0].scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("$[0].value", Matchers.is(10)))
                .andExpect(jsonPath("$[0].guestId", Matchers.is(1)));
    }


}
