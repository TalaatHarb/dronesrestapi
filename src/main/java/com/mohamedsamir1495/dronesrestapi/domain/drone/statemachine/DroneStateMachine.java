package com.mohamedsamir1495.dronesrestapi.domain.drone.statemachine;

import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;

public class DroneStateMachine {

	private DroneStateMachine(){
	}

	public static Drone getNextState(Drone drone){
		return switch (drone.getState()) {
			case LOADED -> moveToDeliveringState(drone);
			case DELIVERING -> moveToDeliveredState(drone);
			case DELIVERED -> moveToReturningState(drone);
			case RETURNING -> moveToIdleState(drone);
			default -> drone;
		};
	}

	private static Drone moveToDeliveringState(Drone drone){
		drone.setState(DroneState.DELIVERING);
		return drone;
	}
	private static Drone moveToDeliveredState(Drone drone){
		drone.setState(DroneState.DELIVERED);
		drone.setBatteryCapacity(drone.getBatteryCapacity()-25);
		return drone;
	}

	private static Drone moveToReturningState(Drone drone){
		drone.setState(DroneState.RETURNING);
		drone.setBatteryCapacity(drone.getBatteryCapacity()-25);
		return drone;
	}

	private static Drone moveToIdleState(Drone drone){
		drone.setState(DroneState.IDLE);
		drone.setBatteryCapacity(100d);
		drone.setCurrentLoadWeight(0);
		return drone;
	}
}
