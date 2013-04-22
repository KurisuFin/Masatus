Feature: Filtering list by author.

  Scenario: Viewing the reference type
    Given Several entries added to database
    When  I go to the main page
    And   I enter author in search box
    And   I submit the search
    Then  List should contain entries with given author
    And   List should not contain entries without given author
