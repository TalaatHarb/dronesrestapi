package com.mohamedsamir1495.dronesrestapi.stepdefinitions;

import com.mohamedsamir1495.dronesrestapi.config.SpringIntegrationTest;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneModel;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.domain.medication.entity.Medication;
import com.mohamedsamir1495.dronesrestapi.domain.medication.enums.MedicationState;
import com.mohamedsamir1495.dronesrestapi.dto.MedicationDTO;
import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import com.mohamedsamir1495.dronesrestapi.repository.MedicationRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckLoadedMedicationStepDefinitions extends SpringIntegrationTest {

	Drone savedDrone;
	List<Medication> savedMedications;

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private DroneRepository droneRepository;

	@Autowired
	private MedicationRepository medicationRepository;

	private WebTestClient.ResponseSpec response;


	@Given("a drone with serial number {string} in {string} state and items {string}")
	public void aDroneWithSerialNumberInState(String serialNumber, String state, String medicationState) {
		savedDrone = Drone.builder()
						   .serialNumber(serialNumber)
						   .weightLimit(50d)
						   .batteryCapacity(100d)
						   .state(DroneState.valueOf(state.toUpperCase()))
						   .model(DroneModel.HEAVYWEIGHT)
						   .build();

		savedDrone = droneRepository.save(savedDrone).block();

		savedMedications = List.of(
				Medication.builder().code("A_1").name("MED_1").weight(5d).droneId(savedDrone.getId()).state(MedicationState.valueOf(medicationState.toUpperCase())).build(),
				Medication.builder().code("B_1").name("MED_2").weight(5d).droneId(savedDrone.getId()).state(MedicationState.valueOf(medicationState.toUpperCase())).build()
		);
		savedMedications = medicationRepository.saveAll(savedMedications).collectList().block();

	}

	@When("the client checks the loaded medication items for the drone with serial {string}")
	public void theClientChecksTheLoadedMedicationItemsForTheDrone(String serialNumber) {
		response = webTestClient.get().uri(String.format("/drones/%s/medications", serialNumber))
								.exchange();

	}

	@Then("the response status code should be ok with the list of loaded medication items")
	public void theResponseShouldContainTheListOfLoadedMedicationItems() {
		List<MedicationDTO> result = response
				.expectStatus().isEqualTo(HttpStatus.OK)
				.expectBody(List.class)
				.returnResult()
				.getResponseBody();
		assertNotNull(result);
		assertFalse(result.isEmpty());
	}

	@Then("the response status code should be ok with no items are loaded")
	public void theResponseShouldIndicateThatNoItemsAreLoaded() {
		var result = response
				.expectStatus().isEqualTo(HttpStatus.OK)
				.expectBody(List.class)
				.returnResult()
				.getResponseBody();
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Given("a drone with serial number {string} in {string} state and No Items Loaded")
	public void aDroneWithSerialNumberInStateAndNoItemsLoaded(String serialNumber, String state) {
		savedDrone = Drone.builder()
						  .serialNumber(serialNumber)
						  .weightLimit(50d)
						  .batteryCapacity(100d)
						  .state(DroneState.valueOf(state.toUpperCase()))
						  .model(DroneModel.HEAVYWEIGHT)
						  .build();

		savedDrone = droneRepository.save(savedDrone).block();
	}
}
