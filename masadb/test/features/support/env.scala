import cucumber.api.scala.{ScalaDsl, EN} 
import play.api.test._
 
import play.api._
import libs.ws.WS
import play.api.mvc._
import play.api.http._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.test.FakeApplication
import play.api.test.FakeRequest
import play.api.test.Helpers._
 
import org.openqa.selenium._
import org.openqa.selenium.firefox._
import org.openqa.selenium.htmlunit._
 
object Env extends ScalaDsl with EN {
 
    var testServer : TestServer = null
    var browser : TestBrowser = null

    Before {
        testServer = TestServer(3333, FakeApplication(additionalConfiguration = inMemoryDatabase()))
        testServer.start()
        browser = TestBrowser.of(Helpers.HTMLUNIT)
    }

    After {
        browser.quit()
        testServer.stop()
    }
}