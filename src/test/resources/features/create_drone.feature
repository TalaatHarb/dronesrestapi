Feature: Create Drone
  As a user
  I want to be able to create a drone
  So that I can manage drones in the system

  Background:
    Given I have an endpoint to create drones

  Scenario: Create a drone successfully
    When I make a POST request to create a drone with serial number "AXA123", weight limit 5.0, battery capacity 75.0, state "IDLE", and model "LIGHTWEIGHT"
    Then the drone should be created successfully

  Scenario: Fail to create a drone with existing serial number
    And a drone with serial number "XYZ123" already exists
    When I make a POST request to create a drone with serial number "XYZ123", weight limit 5.0, battery capacity 75.0, state "IDLE", and model "LIGHTWEIGHT"
    Then the creation should fail with status code 409
