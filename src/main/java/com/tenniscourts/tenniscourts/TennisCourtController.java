package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.config.swagger.annotations.tenniscourt.AddTennisCourtSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.tenniscourt.FindTennisCourtSwaggerInfo;
import com.tenniscourts.config.swagger.annotations.tenniscourt.ListAllTennisCourtsSwaggerInfo;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@AllArgsConstructor
@RequestMapping("/tennis-courts")
@Api("Tennis Court resource, Cceate and find operations")
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @PostMapping
    @AddTennisCourtSwaggerInfo
    public ResponseEntity<Void> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @GetMapping()
    @ListAllTennisCourtsSwaggerInfo
    public ResponseEntity<List<TennisCourtDTO>> findTennisCourts() {
        return ResponseEntity.ok(tennisCourtService.findTennisCourts());
    }

    @GetMapping("/{tennisCourtId}/schedules")
    @FindTennisCourtSwaggerInfo
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
