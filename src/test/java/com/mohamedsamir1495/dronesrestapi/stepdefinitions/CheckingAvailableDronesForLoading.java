package com.mohamedsamir1495.dronesrestapi.stepdefinitions;

import com.mohamedsamir1495.dronesrestapi.config.SpringIntegrationTest;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneModel;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckingAvailableDronesForLoading extends SpringIntegrationTest {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private DroneRepository droneRepository;


	private WebTestClient.ResponseSpec response;


	@Given("multiple drones registered with different states and battery levels")
	public void multipleDronesRegisteredWithDifferentStatesAndBatteryLevels() {
		droneRepository.saveAll(
				List.of(
						Drone.builder().serialNumber("123ABC").weightLimit(50d).batteryCapacity(50d).state(DroneState.IDLE).model(DroneModel.HEAVYWEIGHT).build(),
						Drone.builder().serialNumber("123XYZ").weightLimit(50d).batteryCapacity(50d).state(DroneState.LOADING).model(DroneModel.HEAVYWEIGHT).build()
					)
		).collectList().block();


	}

	@When("the client checks for available drones for loading")
	public void theClientChecksForAvailableDronesForLoading() {
		response = webTestClient.get().uri("/drones/available").exchange();
	}

	@Given("multiple drones registered with all in DELIVERING state")
	public void multipleDronesRegisteredWithAllInDELIVERINGState() {
		droneRepository.deleteAll().block();
		droneRepository.saveAll(
				List.of(
						Drone.builder().serialNumber("123CBA").weightLimit(50d).batteryCapacity(50d).state(DroneState.DELIVERED).model(DroneModel.HEAVYWEIGHT).build(),
						Drone.builder().serialNumber("123ZYX").weightLimit(50d).batteryCapacity(50d).state(DroneState.DELIVERED).model(DroneModel.HEAVYWEIGHT).build()
						)
		).collectList().block();
	}

	@And("the response should indicate that no drones are available")
	public void theResponseShouldIndicateThatNoDronesAreAvailable() {
		var result = response
				.expectStatus().isEqualTo(HttpStatus.OK)
				.expectBody(List.class)
				.returnResult()
				.getResponseBody();

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@And("the response status code should be {int} with a list of available drones")
	public void theResponseShouldContainAListOfAvailableDrones(int statusCode) {
		var result = response
				.expectStatus().isEqualTo(statusCode)
				.expectBody(List.class)
				.returnResult()
				.getResponseBody();

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Given("no drones registered")
	public void noDronesRegistered() {
		droneRepository.deleteAll().block();
	}

	@Then("the response status code should be {int} the response should indicate that no drones are available")
	public void theResponseStatusCodeShouldBeTheResponseShouldIndicateThatNoDronesAreAvailable(int statusCode) {
		var result = response
				.expectStatus().isEqualTo(statusCode)
				.expectBody(List.class)
				.returnResult()
				.getResponseBody();

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}


}
