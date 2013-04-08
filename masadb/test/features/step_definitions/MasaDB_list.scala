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
 
class CucumberSteps extends ScalaDsl with EN {
  Given("""^Set a database$"""){ () =>
    Env.browser.goTo("http://localhost:3333")
    Env.browser.$("button").click()
  }
  When("""^I go to the home page$"""){ () =>
    Env.browser.goTo("http://localhost:3333/") 
  }
}