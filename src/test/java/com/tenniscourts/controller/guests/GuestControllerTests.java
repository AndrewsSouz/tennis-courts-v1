package com.tenniscourts.controller.guests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.guests.GuestController;
import com.tenniscourts.guests.GuestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.tenniscourts.stubs.GuestStubs.createGuestDTOStub;
import static com.tenniscourts.stubs.GuestStubs.guestDTOStub;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {GuestController.class, GuestService.class})
class GuestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GuestService guestService;

    @Test
    void saveGuestShouldReturnStatusCREATED() throws Exception {
        var createGuestDTOStub = createGuestDTOStub();
        var guestDTOStub = guestDTOStub();

        var json = new ObjectMapper().writeValueAsString(createGuestDTOStub);

        when(guestService.saveGuest(createGuestDTOStub)).thenReturn(guestDTOStub);

        this.mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findGuestByIdtShouldReturnStatusOKAndTheGuest() throws Exception {
        var guestDTOStub = guestDTOStub();

        when(guestService.findGuestById(1L)).thenReturn(guestDTOStub);

        this.mockMvc.perform(get("/guests/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Andrews")));
    }

    @Test
    void findByGuestNameShouldReturnStatusOKAndAListOfGuests() throws Exception {
        var guestDTOStub = guestDTOStub();

        when(guestService.findByGuestName("and")).thenReturn(List.of(guestDTOStub));

        this.mockMvc.perform(get("/guests/name/and"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Andrews")));
    }

    @Test
    void findAllGuestsShouldReturnStatusOKAndAListOfGuests() throws Exception {
        var guestDTOStub = guestDTOStub();

        when(guestService.findAllGuests()).thenReturn(List.of(guestDTOStub));

        this.mockMvc.perform(get("/guests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Andrews")));
    }

    @Test
    void updateGuestShouldReturnStatusOKAndTheGuest() throws Exception {
        var createGuestDTOStub = createGuestDTOStub();
        createGuestDTOStub.setName("Andrews Souza");
        var guestDTOStub = guestDTOStub();
        guestDTOStub.setName("Andrews Souza");

        var json = new ObjectMapper().writeValueAsString(createGuestDTOStub);

        when(guestService.updateGuest(1L, createGuestDTOStub)).thenReturn(guestDTOStub);

        this.mockMvc.perform(put("/guests/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Andrews Souza")));
    }

    @Test
    void deleteGuestByIdtShouldReturnStatusNOCONTENT() throws Exception {
        this.mockMvc.perform(delete("/guests/1"))
                .andExpect(status().isNoContent());
    }
}
