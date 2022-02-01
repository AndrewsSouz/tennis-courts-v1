package com.tenniscourts.controller.schedules;

import com.tenniscourts.schedules.ScheduleController;
import com.tenniscourts.schedules.ScheduleFacade;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.tenniscourts.stubs.ScheduleStubs.createScheduleRequestDTOStub;
import static com.tenniscourts.stubs.ScheduleStubs.scheduleDTOStub;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {ScheduleController.class, ScheduleFacade.class})
class ScheduleControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ScheduleFacade scheduleFacade;

    @Test
    void addScheduleShouldReturnStatusCREATED() throws Exception {
        var createScheduleDtoStub = createScheduleRequestDTOStub();
        var scheduleDtoStub = scheduleDTOStub();

        var jsonObject = new JSONObject();
        jsonObject.put("tennisCourtId", createScheduleDtoStub.getTennisCourtId());
        jsonObject.put("startDateTime", createScheduleDtoStub.getStartDateTime());

        var json = jsonObject.toString();

        when(scheduleFacade.addSchedule(createScheduleDtoStub)).thenReturn(scheduleDtoStub);

        this.mockMvc.perform(post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findScheduleByIdShouldReturnASchedule() throws Exception {
        var scheduleDtoStub = scheduleDTOStub();

        when(scheduleFacade.findScheduleById(1L)).thenReturn(scheduleDtoStub);

        this.mockMvc.perform(get("/schedules/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("tennisCourt.name", Matchers.is("Test Court")));
    }

    @Test
    void findFreeSchedulesByDatesShouldReturnAListOfSchedules() throws Exception {
        var scheduleDtoStub = scheduleDTOStub();

        LocalDateTime startDateTime = LocalDateTime.of(2021, 8, 8, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 8, 10, 23, 59);

        when(scheduleFacade.findSchedulesByDates(startDateTime, endDateTime)).thenReturn(List.of(scheduleDtoStub));

        this.mockMvc.perform(get("/schedules/freeslots")
                        .param("startDate", "2021-08-08")
                        .param("endDate", "2021-08-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("$[0].endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("$[0].tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].tennisCourt.name", Matchers.is("Test Court")));
    }

    @Test
    void findFreeSchedulesShouldReturnAListOfSchedules() throws Exception {
        var scheduleDtoStub = scheduleDTOStub();

        LocalDateTime startDateTime = LocalDateTime.of(2021, 8, 8, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 8, 10, 23, 59);

        when(scheduleFacade.findSchedulesByDates(startDateTime, endDateTime)).thenReturn(List.of(scheduleDtoStub));

        this.mockMvc.perform(get("/schedules/freeslots")
                        .param("startDate", "2021-08-08")
                        .param("endDate", "2021-08-10")
                        .param("TennisCourt", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("$[0].endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("$[0].tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].tennisCourt.name", Matchers.is("Test Court")));
    }

    @Test
    void findFreeSchedulesByTennisCourtAndDatesShouldReturnAListOfSchedules() throws Exception {
        var scheduleDtoStub = scheduleDTOStub();

        LocalDateTime startDateTime = LocalDateTime.of(2021, 8, 8, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 8, 10, 23, 59);

        when(scheduleFacade.findFreeSchedulesByTennisCourtAndDates(1L, startDateTime, endDateTime))
                .thenReturn(List.of(scheduleDtoStub));

        this.mockMvc.perform(get("/schedules/freeslots/1")
                        .param("startDate", "2021-08-08")
                        .param("endDate", "2021-08-10")
                        .param("TennisCourt", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("$[0].endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("$[0].tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].tennisCourt.name", Matchers.is("Test Court")));
    }
}
