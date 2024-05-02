package com.mohamedsamir1495.dronesrestapi.exception.drone;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DroneWeightLimitException extends DroneValidationException {

	private static final long serialVersionUID = -8448736977295964019L;

	public DroneWeightLimitException(String serialNumber) {
        super(String.format("The new weight load exceed the current drone [%s]'s weight load", serialNumber));
    }

}
