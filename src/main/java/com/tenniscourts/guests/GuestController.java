package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.config.swagger.annotations.guest.*;
import com.tenniscourts.guests.model.CreateGuestDTO;
import com.tenniscourts.guests.model.GuestDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/guests")
@Api("Guests Resource, operations to manage guests")
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    @CreateGuestSwaggerInfo
    public ResponseEntity<Void> saveGuest(@RequestBody CreateGuestDTO createGuestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.saveGuest(createGuestDTO).getId())).build();
    }

    @GetMapping("/{guestId}")
    @FindGuestSwaggerInfo
    public ResponseEntity<GuestDTO> findGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping("/name/{guestName}")
    @FindGuestByNameSwaggerInfo
    public ResponseEntity<List<GuestDTO>> findByGuestName(@PathVariable String guestName) {
        return ResponseEntity.ok(guestService.findByGuestName(guestName));
    }

    @GetMapping
    @ListAllGuestsSwaggerInfo
    public ResponseEntity<List<GuestDTO>> findAllGuests() {
        return ResponseEntity.ok(guestService.findAllGuests());
    }

    @PutMapping("/{guestId}")
    @UpdateGuestSwaggerInfo
    public ResponseEntity<GuestDTO> updateGuest(@PathVariable Long guestId, @RequestBody CreateGuestDTO createGuestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestId, createGuestDTO));
    }

    @DeleteMapping("/{guestId}")
    @DeleteGuestSwaggerInfo
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }
}
