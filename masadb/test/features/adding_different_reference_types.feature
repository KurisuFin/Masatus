Feature: Different reference types can be added.

  Scenario: Adding an article
    Given A new database
    When  I go to the main page
    And   I open the form for adding a new reference
    And   I choose article as the reference type
    And   I enter reference information
    And   I submit the form
    And   I go to the reference list
    Then  The list should contain the added reference

  Scenario: Viewing the reference type
    Given A tech report reference has been added
    When  I go to the main page
    And   I open link to view the reference
    Then  The reference type should show tech report

  Scenario: Generating bibtex-code
    Given A proceedings reference has been added
    When  I go to the main page
    And   I open link to view the reference
    Then  The page should contain BibTex code for the proceedings reference