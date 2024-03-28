Feature: Checking Available Drones for Loading

  Scenario: Successfully check available drones for loading
    Given multiple drones registered with different states and battery levels
    When the client checks for available drones for loading
    Then the response status code should be 200 with a list of available drones

  Scenario: Check available drones for loading when all drones are busy
    Given multiple drones registered with all in DELIVERING state
    When the client checks for available drones for loading
    Then the response status code should be 200 the response should indicate that no drones are available

  Scenario: Check available drones for loading when no drones are registered
    Given no drones registered
    When the client checks for available drones for loading
    Then the response status code should be 200 the response should indicate that no drones are available
