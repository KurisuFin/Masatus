Feature: MasaDB
  A new book can be added to database.
 
  Scenario: Empty database has no books
    Given A new database
    When  I go to the book list
    Then  Book list should have no books

  Scenario: Adding the first book
    Given A new database
    When  I go to the main page
    And   I open the form for adding a new book
    And   I submit book information
    And   I go to the book list
    Then  Book list should contain the book
