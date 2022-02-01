package com.tenniscourts.tenniscourts.model;

import com.tenniscourts.schedules.model.ScheduleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TennisCourtDTO {

    @ApiModelProperty("Tennis court identifier")
    private Long id;

    @NotNull
    @ApiModelProperty("Tennis court name")
    private String name;

    @ApiModelProperty("Tennis court schedules")
    private List<ScheduleDTO> tennisCourtSchedules;

}
