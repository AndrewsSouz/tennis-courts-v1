package com.tenniscourts.controller.tenniscourts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.tenniscourts.TennisCourtController;
import com.tenniscourts.tenniscourts.TennisCourtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.tenniscourts.stubs.ScheduleStubs.scheduleDTOStub;
import static com.tenniscourts.stubs.TennisCourtStubs.tennisCourtDTOStub;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {TennisCourtController.class, TennisCourtService.class})
class TennisCourtControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TennisCourtService tennisCourtService;


    @Test
    void addTennisCourtShouldReturnStatusCREATED() throws Exception {
        var tennisCourtDTOStub = tennisCourtDTOStub();
        when(tennisCourtService.addTennisCourt(tennisCourtDTOStub)).thenReturn(tennisCourtDTOStub);
        var json = new ObjectMapper().writeValueAsString(tennisCourtDTOStub);
        this.mockMvc.perform(post("/tennis-courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findTennisCourtWitchSchedulesByIdShouldReturnStatusOKAndATennisCourt() throws Exception {
        var tennisCourtDTOStub = tennisCourtDTOStub();
        tennisCourtDTOStub.setTennisCourtSchedules(List.of(scheduleDTOStub()));
        when(tennisCourtService.findTennisCourtWithSchedulesById(1L)).thenReturn(tennisCourtDTOStub);
        this.mockMvc.perform(get("/tennis-courts/1/schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Test Court")))
                .andExpect(jsonPath("tennisCourtSchedules[0].id", Matchers.is(1)))
                .andExpect(jsonPath("tennisCourtSchedules[0].tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("tennisCourtSchedules[0].tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("tennisCourtSchedules[0].startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("tennisCourtSchedules[0].endDateTime", Matchers.is("2021-08-08T09:00")));
    }

    @Test
    void findTennisCourtsShouldReturnStatusOKAndTheTennisCourts() throws Exception {
        var tennisCourtDTOStub = tennisCourtDTOStub();
        tennisCourtDTOStub.setTennisCourtSchedules(List.of(scheduleDTOStub()));
        when(tennisCourtService.findTennisCourts()).thenReturn(List.of(tennisCourtDTOStub));
        this.mockMvc.perform(get("/tennis-courts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", Matchers.is(1)))
                .andExpect(jsonPath("[0].name", Matchers.is("Test Court")))
                .andExpect(jsonPath("[0].tennisCourtSchedules[0].id", Matchers.is(1)))
                .andExpect(jsonPath("[0].tennisCourtSchedules[0].tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("[0].tennisCourtSchedules[0].tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("[0].tennisCourtSchedules[0].startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("[0].tennisCourtSchedules[0].endDateTime", Matchers.is("2021-08-08T09:00")));
    }
}
