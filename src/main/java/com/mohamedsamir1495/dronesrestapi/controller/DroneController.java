package com.mohamedsamir1495.dronesrestapi.controller;

import com.mohamedsamir1495.dronesrestapi.dto.DroneDTO;
import com.mohamedsamir1495.dronesrestapi.dto.MedicationDTO;
import com.mohamedsamir1495.dronesrestapi.service.DroneService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/drones")
public class DroneController {

    private final DroneService droneService;

    @PostMapping
    public Mono<ResponseEntity<DroneDTO>> registerDrone(@Valid @RequestBody DroneDTO droneDTO) {
        return droneService.registerDrone(droneDTO).map(createdDto -> ResponseEntity.status(HttpStatus.CREATED).body(createdDto));
    }

    @PostMapping("/{serialNumber}/medications")
    public Mono<ResponseEntity<List<MedicationDTO>>> loadMedication(@PathVariable String serialNumber,@Valid @RequestBody List<MedicationDTO> medicationDTO) {
        return droneService.loadMedication(serialNumber, medicationDTO)
                           .collectList()
                           .map(createdMedications -> ResponseEntity.status(HttpStatus.CREATED).body(createdMedications));
    }

    @GetMapping("/{serialNumber}/medications")
    public Mono<ResponseEntity<List<MedicationDTO>>> getLoadedMedications(@PathVariable String serialNumber) {
        return droneService.getLoadedMedications(serialNumber)
                .collectList()
                .map(savedMedications -> ResponseEntity.status(HttpStatus.OK).body(savedMedications));

    }

    @GetMapping("/available")
    public Flux<DroneDTO> getAvailableDrones() {
       return droneService.getAvailableDrones();
    }

    @GetMapping("/{serialNumber}/battery")
    public Mono<ResponseEntity<Double>> getDroneBatteryLevel(@PathVariable String serialNumber) {
        // Implementation
        return droneService.getDroneBatteryLevel(serialNumber).map(batteryCapacity-> ResponseEntity.status(HttpStatus.OK).body(batteryCapacity));
    }
}
