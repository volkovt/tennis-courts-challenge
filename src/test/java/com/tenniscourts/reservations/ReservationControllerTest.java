package com.tenniscourts.reservations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService service;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateReservationRequestDTO createReservationRequestDTO;
    private ReservationDTO reservationDTO;

    private Long existingId;
    private Long nonExistingId;
    private Long validGuestId;
    private Long validScheduleId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        validGuestId = 1L;
        validScheduleId = 1L;

        createReservationRequestDTO = CreateReservationRequestDTO.builder()
                .scheduleId(validScheduleId)
                .guestId(validGuestId)
                .build();

        reservationDTO = ReservationDTO.builder().build();

        when(service.findReservation(existingId)).thenReturn(ReservationDTO.builder().build());
        when(service.findReservation(nonExistingId)).thenThrow(EntityNotFoundException.class);
        when(service.cancelReservation(nonExistingId)).thenThrow(EntityNotFoundException.class);
        when(service.cancelReservation(existingId)).thenReturn(reservationDTO);
    }

    @Test
    public void findReservationShouldReturnReservationWhenValidId() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/reservations/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void findReservationShouldReturnNotFoundWhenInvalidId() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/reservations/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    /*TODO: This test will only work with the correct implementation of bookReservation.*/
    @Test
    public void bookReservationShouldReturnCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(createReservationRequestDTO);
        ResultActions result =
                mockMvc.perform(post("/reservations")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
    }

    @Test
    public void cancelReservationShouldReturnNotFoundWhenInvalidId() throws Exception {

        ResultActions result =
                mockMvc.perform(delete("/reservations/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void cancelReservationShouldReturnNoContentWhenValidId() throws Exception {

        ResultActions result =
                mockMvc.perform(delete("/reservations/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }
}
