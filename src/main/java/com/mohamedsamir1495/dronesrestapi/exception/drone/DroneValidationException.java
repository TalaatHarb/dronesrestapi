package com.mohamedsamir1495.dronesrestapi.exception.drone;

public class DroneValidationException extends RuntimeException {

	private static final long serialVersionUID = -4664659911565251554L;

	public DroneValidationException(String msg) {
		super(msg);
	}
}
