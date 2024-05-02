package com.mohamedsamir1495.dronesrestapi.stepdefinitions;

import com.mohamedsamir1495.dronesrestapi.config.SpringIntegrationTest;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneModel;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.domain.medication.enums.MedicationState;
import com.mohamedsamir1495.dronesrestapi.dto.ErrorResponseDto;
import com.mohamedsamir1495.dronesrestapi.dto.MedicationDTO;
import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import com.mohamedsamir1495.dronesrestapi.repository.MedicationRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.List;

public class LoadMedicationStepDefinitions extends SpringIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    private WebTestClient.ResponseSpec response;

    List<MedicationDTO> medicationToBeSaved;
    List<MedicationDTO> savedMedications;


    @Given("a drone with serial number {string} in {string} state and battery level above {double}%")
    public void aDroneWithSerialNumberInIDLEStateAndBatteryLevelAbove(String serialNumber, String state, double batteryCapacity ) {
        Drone savedDrone = new Drone();
        savedDrone.setSerialNumber(serialNumber);
        savedDrone.setWeightLimit(100d);
        savedDrone.setBatteryCapacity(batteryCapacity);
        savedDrone.setState(DroneState.valueOf(state.toUpperCase()));
        savedDrone.setModel(DroneModel.LIGHTWEIGHT);
        droneRepository.save(savedDrone).block();
    }

    @And("medication items to load with total weight within drone's weight limit")
    public void medicationItemsToLoadWithTotalWeightWithinDroneSWeightLimit() {
        medicationToBeSaved = List.of(
                MedicationDTO.builder().code("A_1").name("MED_1").weight(5d).build(),
                MedicationDTO.builder().code("B_1").name("MED_2").weight(5d).build()
        );
    }

    @When("the client loads the drone {string} with medication items")
    public void theClientLoadsTheDroneWithMedicationItems(String serialNumber) {
        response = webTestClient.post().uri(String.format("/drones/%s/medications", serialNumber))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(medicationToBeSaved)
                                .exchange();
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        savedMedications = response
                .expectStatus().isEqualTo(statusCode)
                .expectBody(new ParameterizedTypeReference<List<MedicationDTO>>() {})
                .returnResult()
                .getResponseBody();
    }

    @And("the drone's state with serial {string} should change to {string}")
    public void theDroneSStateShouldChangeToLOADING(String serialNumber, String state) {
        droneRepository.findBySerialNumber(serialNumber)
                       .as(StepVerifier::create)
                       .expectNextMatches(drone -> drone.getState().name().equalsIgnoreCase(state))
                       .verifyComplete();
    }

    @And("the medication items should be successfully loaded onto the drone with serial {string} should change to {string}")
    public void theMedicationItemsShouldBeSuccessfullyLoadedOntoTheDrone(String serialNumber,String state) {
        droneRepository.findBySerialNumber(serialNumber)
                       .map(Drone::getId)
                       .flatMapMany(id -> medicationRepository.findByDroneIdAndState(id, MedicationState.LOADED))
                       .as(StepVerifier::create)
                       .expectNextMatches(medication -> medication.getState().name().equalsIgnoreCase(state))
                       .expectNextMatches(medication -> medication.getState().name().equalsIgnoreCase(state))
                       .verifyComplete();
    }

    @And("medication items to load exceeding drone's weight limit")
    public void medicationItemsToLoadExceedingDroneSWeightLimit() {
        medicationToBeSaved = List.of(
                MedicationDTO.builder().code("A_1").name("MED_1").weight(5000d).build(),
                MedicationDTO.builder().code("B_1").name("MED_2").weight(5000d).build()
        );
    }

    @Then("the response status code should indicate failure with code {int}")
    public void theResponseStatusCodeShouldIndicateFailureWithCode(int statusCode) {
        response
                .expectStatus().isEqualTo(statusCode)
                .expectBody(ErrorResponseDto.class)
                .returnResult()
                .getResponseBody();
    }

    @Given("a drone with serial number {string} in IDLE state and battery level below 25% ex: {double}")
    public void aDroneWithSerialNumberInIDLEStateAndBatteryLevelBelowEx(String serialNumber, double batteryCapacity) {
        Drone savedDrone = new Drone();
        savedDrone.setSerialNumber(serialNumber);
        savedDrone.setWeightLimit(100d);
        savedDrone.setBatteryCapacity(batteryCapacity);
        savedDrone.setState(DroneState.IDLE);
        savedDrone.setModel(DroneModel.LIGHTWEIGHT);
        droneRepository.save(savedDrone).block();
    }

    @Given("the user has the wrong serial number for a drone {string} that is not found in the database")
    public void theUserHasTheWrongSerialNumberForADroneThatIsNotFoundInTheDatabase(String serialNumber) {
    }
}
