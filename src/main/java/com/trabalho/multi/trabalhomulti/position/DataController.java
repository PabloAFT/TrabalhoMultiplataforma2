package com.trabalho.multi.trabalhomulti.position;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.cassandra.service.Cassandra.remove_args;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trabalho.multi.trabalhomulti.vehicle.Vehicle;
import com.trabalho.multi.trabalhomulti.vehicle.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class DataController {
    @Autowired
    DataByVehicleService dataByVehicleService;

    @Autowired
    VehicleService vehicleService;

    @PostMapping("/randomize-positions")
    ResponseEntity<String> randomizePositions() {
        try {
            dataByVehicleService.generateMockData();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/{vehicleId}/positions/last")
    ResponseEntity<Position> getLastPositionByVehicleId(@PathVariable UUID vehicleId) {
        try {
            return ResponseEntity.ok().body(dataByVehicleService.getLastPositionByVehicleId(vehicleId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/positions/last")
    ResponseEntity<List<DataByVehicle>> getLastPositions() {
        try {
            return ResponseEntity.ok().body(dataByVehicleService.getAllLastPositions());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
