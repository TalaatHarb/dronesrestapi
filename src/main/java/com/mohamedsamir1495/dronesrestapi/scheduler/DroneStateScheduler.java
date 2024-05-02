package com.mohamedsamir1495.dronesrestapi.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.domain.drone.statemachine.DroneStateMachine;
import com.mohamedsamir1495.dronesrestapi.domain.medication.enums.MedicationState;
import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import com.mohamedsamir1495.dronesrestapi.repository.MedicationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class DroneStateScheduler {

	private final DroneRepository droneRepository;
	private final MedicationRepository medicationRepository;

	@Scheduled(fixedRateString = "${scheduler.drone-state-updater}")
	public void updateDroneState() {
		log.debug("Drone state updater triggered");
		droneRepository.findAll()
					   .flatMap(this::saveDroneState)
					   .collectList()
					   .block();
	}

	private Mono<Drone> saveDroneState(Drone drone) {
		return droneRepository.save(DroneStateMachine.getNextState(drone)).doOnNext(this::updateMedicationStateIfDelivered);
	}

	private void updateMedicationStateIfDelivered(Drone drone) {
		if (DroneState.DELIVERED.equals(drone.getState())) {
			medicationRepository.updateMedicationState(MedicationState.DELIVERED, drone.getId()).subscribe();
		}
	}
}
