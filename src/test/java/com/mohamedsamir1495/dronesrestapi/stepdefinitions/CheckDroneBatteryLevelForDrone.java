package com.mohamedsamir1495.dronesrestapi.stepdefinitions;

import com.mohamedsamir1495.dronesrestapi.config.SpringIntegrationTest;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneModel;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckDroneBatteryLevelForDrone extends SpringIntegrationTest {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private DroneRepository droneRepository;

	Drone savedDrone;

	private WebTestClient.ResponseSpec response;

	@Given("a drone with serial number {string} and battery level at {double}%")
	public void aDroneWithSerialNumberAndBatteryLevelAt(String serialNumber, Double batteryCapacity) {
		savedDrone = Drone.builder()
						  .serialNumber(serialNumber)
						  .weightLimit(50d)
						  .batteryCapacity(batteryCapacity)
						  .state(DroneState.IDLE)
						  .model(DroneModel.HEAVYWEIGHT)
						  .build();

		savedDrone = droneRepository.save(savedDrone).block();
	}

	@When("the client checks the battery level for the drone with serial number {string}")
	public void theClientChecksTheBatteryLevelForTheDrone(String serialNumber) {
		response = webTestClient.get().uri(String.format("/drones/%s/battery", serialNumber)).exchange();
	}


	@Then("the response status code should be ok with the battery level {double}")
	public void theResponseStatusCodeShouldBeWithTheBatteryLevelInformation(Double batteryCapacity) {
		String result = response
				.expectStatus().isEqualTo(HttpStatus.OK)
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
		assertEquals(Double.valueOf(result), batteryCapacity);
	}

	@Given("a drone with serial number {string} not registered")
	public void aDroneWithSerialNumberNotRegistered(String arg0) {

	}

	@Then("the response status check battery level api call should indicate failure with code {int}")
	public void theResponseStatusCheckBatteryLevelApiCallShouldIndicateFailureWithCode(int statusCode) {
		response
				.expectStatus().isEqualTo(statusCode)
				.expectBody(String.class)
				.returnResult();
	}
}
