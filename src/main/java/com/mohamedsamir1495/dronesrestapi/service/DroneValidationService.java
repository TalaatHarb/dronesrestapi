package com.mohamedsamir1495.dronesrestapi.service;

import com.mohamedsamir1495.dronesrestapi.dto.MedicationDTO;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.exception.drone.DroneLoadingStateException;
import com.mohamedsamir1495.dronesrestapi.exception.drone.DroneLowBatteryException;
import com.mohamedsamir1495.dronesrestapi.exception.drone.DroneWeightLimitException;
import reactor.core.publisher.Mono;

import java.util.List;

public class DroneValidationService {
	private DroneValidationService(){}

	public static Mono<Drone> validateDroneState(Drone drone) {
		if (DroneState.LOADING.equals(drone.getState())) {
			return Mono.error(new DroneLoadingStateException(drone.getSerialNumber()));
		}
		return Mono.just(drone);
	}

	public static Mono<Drone> validateBattery(Drone drone) {
		if (drone.getBatteryCapacity() < 25) {
			return Mono.error(new DroneLowBatteryException(drone.getSerialNumber()));
		}
		return Mono.just(drone);
	}

	public static Mono<Drone> validateWeightCapacity(Drone drone, List<MedicationDTO> medicationDTO) {
		if (!isCurrentDroneWeightCapacityEnoughForNewLoad(drone, medicationDTO)) {
			return Mono.error(new DroneWeightLimitException(drone.getSerialNumber()));
		}
		return Mono.just(drone);
	}

	private static boolean isCurrentDroneWeightCapacityEnoughForNewLoad(Drone drone,List<MedicationDTO> medicationList) {
		double sumOfNewLoadWeight = medicationList.stream()
												  .map(MedicationDTO::getWeight)
												  .mapToDouble(x->x)
												  .sum();
		return drone.getWeightLimit() >= drone.getCurrentLoadWeight() + sumOfNewLoadWeight;
	}
}
