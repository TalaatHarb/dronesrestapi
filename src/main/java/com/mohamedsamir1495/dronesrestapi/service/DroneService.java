package com.mohamedsamir1495.dronesrestapi.service;

import com.mohamedsamir1495.dronesrestapi.dto.DroneDTO;
import com.mohamedsamir1495.dronesrestapi.dto.MedicationDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DroneService {

	Mono<DroneDTO> registerDrone(DroneDTO droneDTO);

	Flux<MedicationDTO> getLoadedMedications(String serialNumber);

	Flux<MedicationDTO> loadMedication(String serialNumber, List<MedicationDTO> medicationDTO);

	Flux<DroneDTO> getAvailableDrones();

	Mono<Double> getDroneBatteryLevel(String serialNumber);
}
