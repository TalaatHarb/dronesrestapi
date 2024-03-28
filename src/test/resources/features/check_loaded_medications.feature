Feature: Checking Loaded Medication Items for a Given Drone

  Scenario: Successfully check loaded medication items for a given drone
    Given a drone with serial number "123ABC" in "LOADED" state and items "LOADED"
    When the client checks the loaded medication items for the drone with serial "123ABC"
    Then the response status code should be ok with the list of loaded medication items

  Scenario: Check loaded medication items for a drone with no items loaded
    Given a drone with serial number "456ABC" in "IDLE" state and No Items Loaded
    When the client checks the loaded medication items for the drone with serial "456ABC"
    Then the response status code should be ok with no items are loaded

  Scenario: Check loaded medication items for a drone with items Delivered
    Given a drone with serial number "789ABC" in "IDLE" state and items "DELIVERED"
    When the client checks the loaded medication items for the drone with serial "789ABC"
    Then the response status code should be ok with no items are loaded
