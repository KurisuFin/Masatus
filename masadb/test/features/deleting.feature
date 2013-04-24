Feature: Book in database can be removed.

  Scenario: Removing an article
    Given Several entries added to database
    When  I go to the main page
    And   I open first reference to view
    And   I click delete button
    Then  The list should not have deleted entry
