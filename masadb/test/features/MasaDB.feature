Feature: MasaDB
  In order to list books, one book must be added to database.
 
  Scenario: Adds book to database and lists
    Given Set a database
    When I go to the home page
    Then I should see "0 books"
    When I go to the add form in "label" with "add form"    
    And  I push "add"
    Then I should see "1 books"
