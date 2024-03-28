package com.mohamedsamir1495.dronesrestapi.exception.drone;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class DroneLoadingStateException extends DroneValidationException {
    public DroneLoadingStateException(String serialNumber) {
        super(String.format("Drone [%s] is currently in loading state ", serialNumber));
    }
}
