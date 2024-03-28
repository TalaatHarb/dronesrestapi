Feature: Load Medications
  As a user
  I want to be able to load medications to drones already registered in db given i have their serial number

  Scenario: Successfully load a drone with medication items
    Given a drone with serial number "123ABC" in "IDLE" state and battery level above 25%
    And medication items to load with total weight within drone's weight limit
    When the client loads the drone "123ABC" with medication items
    Then the response status code should be 201
    And the drone's state with serial "123ABC" should change to "LOADED"
    And the medication items should be successfully loaded onto the drone with serial "123ABC" should change to "LOADED"

  Scenario: Attempt to load a drone with state LOADING
    Given a drone with serial number "123CBA" in "LOADING" state and battery level above 25%
    And medication items to load with total weight within drone's weight limit
    When the client loads the drone "123CBA" with medication items
    Then the response status code should indicate failure with code 403

  Scenario: Attempt to load a drone with excess weight
    Given a drone with serial number "123DBC" in "IDLE" state and battery level above 25%
    And medication items to load exceeding drone's weight limit
    When the client loads the drone "123DBC" with medication items
    Then the response status code should indicate failure with code 400

  Scenario: Attempt to load a drone with low battery
    Given a drone with serial number "123XYZ" in IDLE state and battery level below 25% ex: 24
    And medication items to load with total weight within drone's weight limit
    When the client loads the drone "123XYZ" with medication items
    Then the response status code should indicate failure with code 400

  Scenario: Attempt to load a drone that is not found in the database
    Given the user has the wrong serial number for a drone "123AAA" that is not found in the database
    And medication items to load with total weight within drone's weight limit
    When the client loads the drone "123AAA" with medication items
    Then the response status code should indicate failure with code 404
