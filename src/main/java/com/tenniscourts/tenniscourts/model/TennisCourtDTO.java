package com.tenniscourts.tenniscourts.model;

import com.tenniscourts.schedules.model.ScheduleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TennisCourtDTO {

    @ApiModelProperty("Tennis court identifier")
    private Long id;

    @NotNull
    @ApiModelProperty("Tennis court name")
    private String name;

    @ApiModelProperty("Tennis court schedules")
    private List<ScheduleDTO> tennisCourtSchedules;

}
