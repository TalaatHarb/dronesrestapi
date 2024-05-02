package com.mohamedsamir1495.dronesrestapi.exception.drone;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DroneLowBatteryException extends DroneValidationException {

	private static final long serialVersionUID = 5697974262589808610L;

	public DroneLowBatteryException(String serialNumber) {
        super(String.format("Drone [%s]'s battery power is not enough to move to loading state", serialNumber));
    }

}
