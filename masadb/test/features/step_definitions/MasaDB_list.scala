//import cucumber.runtime.{EN, ScalaDsl, PendingException}
import cucumber.api.scala.{ScalaDsl, EN}
import play.api.test._
 
import play.api._
import libs.ws.WS
import play.api.mvc._
import play.api.http._
 
import play.api.libs.iteratee._
import play.api.libs.concurrent._
 
import org.openqa.selenium._
import org.openqa.selenium.firefox._
import org.openqa.selenium.htmlunit._

import junit.framework.Assert._
import org.fluentlenium.core.filter.FilterConstructor.withText
import Env._

/*
 * https://github.com/FluentLenium/FluentLenium
 *
 * html-elementtien etsintä:
 * id:n perusteella: $("#myid")
 * tagin perusteella: $("a")
 * luokan perusteella: $(".myclass")
 * tekstin perusteella: $("a", withText("mytext"))
 *
 * Screenshottien otto debuggausta varten:
 * 1) selaimen vaihto (env.scala): browser = TestBrowser.of(Helpers.FIREFOX)
 * 2) browser.takeScreenShot("/tmp/pic.png")
 */

class CucumberSteps extends ScalaDsl with EN {
    Given("""^A new database$"""){ () =>
    }

    When("""^I go to the main page$"""){ () =>
        browser.goTo("http://localhost:3333/")
        assertEquals("MasaDB - Kaikki lähdeviitteet", browser.title());
    }

    And("""^I open the form for adding a new book$"""){ () =>
        browser.$("#add").click()
        assertEquals("MasaDB - Uusi lähdeviite", browser.title());
    }

    And("""^I submit book information$"""){ () =>
        browser.$("#title").text("Book Title")
        browser.$("#author").text("Author Name")
        browser.$("#publisher").text("The Publisher")
        browser.$("#year").text("2099")
        browser.$("#citeKey").text("bt99")
        browser.$("#save").click() //Env.browser.submit("#save")
        assertEquals("MasaDB - Kaikki lähdeviitteet", browser.title())
    }

    And("""^I go to the book list$"""){ () =>
        browser.goTo("http://localhost:3333/")
        assertEquals("MasaDB - Kaikki lähdeviitteet", browser.title());
    }

    Then("""^Book list should contain the book$"""){ () =>
        assertFalse(browser.$("td", withText().contains("Book Title")).isEmpty())
        assertFalse(browser.$("td", withText().contains("Author Name")).isEmpty())
    }

    Then("""^Book list should have no books$"""){ () =>
        assertFalse(browser.$("p", withText().contains(
                    "Tietokantaan ei ole lisätty vielä yhtään viitettä.")).isEmpty())
    }
}