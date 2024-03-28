package com.mohamedsamir1495.dronesrestapi.service;

import com.mohamedsamir1495.dronesrestapi.domain.medication.enums.MedicationState;
import com.mohamedsamir1495.dronesrestapi.dto.DroneDTO;
import com.mohamedsamir1495.dronesrestapi.dto.MedicationDTO;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.exception.drone.DuplicateDroneException;
import com.mohamedsamir1495.dronesrestapi.exception.generic.ResourceNotFoundException;
import com.mohamedsamir1495.dronesrestapi.mapper.DroneMapper;
import com.mohamedsamir1495.dronesrestapi.mapper.MedicationMapper;
import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import com.mohamedsamir1495.dronesrestapi.repository.MedicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;

    @Override
    public Mono<DroneDTO> registerDrone(DroneDTO droneDTO) {
        Drone drone = droneMapper.toEntity(droneDTO);
        return droneRepository.save(drone)
                              .doOnError(e -> {
                                  if (e instanceof DuplicateKeyException) {
                                      throw new DuplicateDroneException(droneDTO.getSerialNumber());
                                  }
                              })
                              .map(droneMapper::toDTO);
    }

    @Override
    public Flux<MedicationDTO> getLoadedMedications(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber)
                              .switchIfEmpty(Mono.error(new ResourceNotFoundException("Drones", "serial number", serialNumber)))
                              .flatMapMany(drone -> medicationRepository.findByDroneIdAndState(drone.getId(), MedicationState.LOADED)).map(medicationMapper::toDTO);
    }

    @Override
    public Flux<MedicationDTO> loadMedication(String serialNumber, List<MedicationDTO> medicationDTO) {
        return droneRepository.findBySerialNumber(serialNumber)
                              .switchIfEmpty(Mono.error(new ResourceNotFoundException("Drone", "serial number", serialNumber)))
                              .flatMap(DroneValidationService::validateDroneState)
                              .flatMap(DroneValidationService::validateBattery)
                              .flatMap(drone -> DroneValidationService.validateWeightCapacity(drone, medicationDTO))
                              .flatMapMany(drone -> {
                                  drone.setState(DroneState.LOADING);
                                  return droneRepository.save(drone)
                                                        .thenMany(medicationRepository.saveAll(medicationDTO.stream().map(dto ->medicationMapper.toEntity(dto,drone)).toList()).map(medicationMapper::toDTO))
                                                        .collectList()
                                                        .flatMapMany(savedMeds -> {
                                                            drone.setState(DroneState.LOADED);
                                                            double sumOfNewLoadWeight = medicationDTO.stream()
                                                                                                     .map(MedicationDTO::getWeight)
                                                                                                     .mapToDouble(x -> x)
                                                                                                     .sum();
                                                            drone.setCurrentLoadWeight(drone.getCurrentLoadWeight() + sumOfNewLoadWeight);
                                                            return droneRepository.save(drone).thenMany(Flux.fromIterable(savedMeds));
                                                        });
                              });
    }



    @Override
    public Flux<DroneDTO> getAvailableDrones() {
        return droneRepository.findByState(DroneState.IDLE).map(droneMapper::toDTO);
    }

    @Override
    public Mono<Double> getDroneBatteryLevel(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber)
                              .switchIfEmpty(Mono.error(new ResourceNotFoundException("Drones", "serial number", serialNumber)))
                              .map(Drone::getBatteryCapacity);
    }

}
