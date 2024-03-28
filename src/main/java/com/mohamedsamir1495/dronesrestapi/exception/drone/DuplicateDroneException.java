package com.mohamedsamir1495.dronesrestapi.exception.drone;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateDroneException extends DroneValidationException {
    public DuplicateDroneException(String serialNumber) {
        super(String.format("Can not register a drone with serial [%s] as already exists in db", serialNumber));
    }
}
