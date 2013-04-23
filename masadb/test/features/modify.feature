Feature: Book in database can be modified.

  Scenario: Modifying a book
    Given Several entries added to database
    When  I go to the main page
    And   I open first reference to view
    And   I clik modify button
    And   I modify reference data
    And   I submit the edit form
    And   I open first reference to view
    Then  The page should show modified data in entry
