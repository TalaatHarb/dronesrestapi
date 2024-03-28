Feature: Check Drone Battery Level for a Given Drone

  Scenario: Successfully check drone battery level
    Given a drone with serial number "123ABC" and battery level at 50%
    When the client checks the battery level for the drone with serial number "123ABC"
    Then the response status code should be ok with the battery level 50

  Scenario: Attempt to check battery level for an unregistered drone
    Given a drone with serial number "XXXXXX" not registered
    When the client checks the battery level for the drone with serial number "XXXXXX"
    Then the response status check battery level api call should indicate failure with code 404
