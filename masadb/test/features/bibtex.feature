Feature: All Books can be downloaded as BibTex.

  Scenario: Downloading a BibTex file with entry
    Given A new database
    When  I go to the main page
    And   I open the form for adding a new reference
    And   I choose book as the reference type
    And   I enter reference information
    And   I submit the form
    And   I go to the reference list
    And   I download BibTex file
    Then  The browser starts downloading a BibTex file
