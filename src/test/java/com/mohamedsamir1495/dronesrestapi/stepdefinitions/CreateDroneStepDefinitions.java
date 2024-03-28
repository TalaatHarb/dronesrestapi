package com.mohamedsamir1495.dronesrestapi.stepdefinitions;

import com.mohamedsamir1495.dronesrestapi.config.SpringIntegrationTest;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneModel;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import com.mohamedsamir1495.dronesrestapi.dto.DroneDTO;
import com.mohamedsamir1495.dronesrestapi.dto.ErrorResponseDto;
import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateDroneStepDefinitions extends SpringIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DroneRepository droneRepository;
    DroneDTO droneDTO = new DroneDTO();

    private Drone savedDrone;

    private WebTestClient.ResponseSpec response;


    @Given("^I have an endpoint to create drones$")
    public void iHaveAWebFluxEndpointToCreateDrones() {
        assertNotNull(webTestClient);
    }


    @When("^I make a POST request to create a drone with serial number \"([^\"]*)\", weight limit ([^\"]*), battery capacity ([^\"]*), state \"([^\"]*)\", and model \"([^\"]*)\"$")
    public void iMakeAPostRequestToCreateADrone(String serialNumber, Double weightLimit, Double batteryCapacity, String state, String model) {
        droneDTO.setSerialNumber(serialNumber);
        droneDTO.setWeightLimit(weightLimit);
        droneDTO.setBatteryCapacity(batteryCapacity);
        droneDTO.setState(state);
        droneDTO.setModel(model);

        response = webTestClient.post().uri("/drones")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .bodyValue(droneDTO)
                                  .exchange();
    }

    @Then("^the drone should be created successfully$")
    public void theDroneShouldBeCreatedSuccessfully() {
        savedDrone = response
                .expectStatus().isCreated()
                .expectBody(Drone.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(savedDrone);

        // Assert that the drone has been inserted into the repository
        droneRepository.findBySerialNumber(savedDrone.getSerialNumber())
                       .as(StepVerifier::create)
                       .expectNextCount(1)
                       .verifyComplete();
    }

    @Then("^the creation should fail with status code (\\d+)$")
    public void theCreationShouldFailWithStatusCode(int statusCode) {
        response
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorResponseDto.class)
                .returnResult()
                .getResponseBody();

    }

    @And("a drone with serial number {string} already exists")
    public void aDroneWithSerialNumberAlreadyExists(String serialNumber) {
        Drone savedDrone = new Drone();
        savedDrone.setSerialNumber(serialNumber);
        savedDrone.setWeightLimit(100d);
        savedDrone.setBatteryCapacity(0d);
        savedDrone.setState(DroneState.IDLE);
        savedDrone.setModel(DroneModel.LIGHTWEIGHT);
        droneRepository.save(savedDrone).block();
    }

}
