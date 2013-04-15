Feature: A new book can be added to database.

  Scenario: Empty database has no books
    Given A new database
    When  I go to the reference list
    Then  The list should have no entries

  Scenario: Adding the first book
    Given A new database
    When  I go to the main page
    And   I open the form for adding a new reference
    And   I choose book as the reference type
    And   I enter reference information
    And   I submit the form
    And   I go to the reference list
    Then  The list should contain the added reference
