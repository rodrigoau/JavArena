# language: en
Feature: Mobile Login Automation
  As a user of the mobile app
  I want to log in with my credentials
  So that I can access my account successfully

  Scenario: Successful login with valid credentials from Json File
    Given I open the mobile application
    When I enter my credentials from the JSON file and submit them
    Then I should see the success access message