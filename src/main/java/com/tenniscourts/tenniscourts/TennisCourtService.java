package com.tenniscourts.tenniscourts;

import com.tenniscourts.common.Utils;
import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TennisCourtService {

    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleService scheduleService;

    private final TennisCourtMapper tennisCourtMapper;

    public TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt) {
        existsByName(tennisCourt.getName());
        return tennisCourtMapper.map(tennisCourtRepository.saveAndFlush(tennisCourtMapper.map(tennisCourt)));
    }

    public List<TennisCourtDTO> findTennisCourts() {
        return Utils.verifyEmptyList(
                tennisCourtRepository.findAll().stream()
                        .map(tennisCourtMapper::map)
                        .collect(Collectors.toList()));
    }

    private TennisCourtDTO findTennisCourtById(Long id) {
        return tennisCourtRepository.findById(id).map(tennisCourtMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found.");
        });
    }

    public TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId) {
        var tennisCourtDTO = findTennisCourtById(tennisCourtId);
        tennisCourtDTO.setTennisCourtSchedules(scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
        return tennisCourtDTO;
    }

    public void validateTennisCourt(Long tennisCourtId) {
        if (!tennisCourtRepository.existsById(tennisCourtId))
            throw new EntityNotFoundException("Tennis court not exists");
    }

    private void existsByName(String name) {
        if (tennisCourtRepository.existsByName(name))
            throw new AlreadyExistsEntityException("Tennis court already registered");
    }
}
