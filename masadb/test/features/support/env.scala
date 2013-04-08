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
 
object Env extends ScalaDsl with EN {
 
  // This is for starting server and stop server.
  // Actually I'm not sure this is correct way.
  var testServer = TestServer(3333)
  var browser = TestBrowser.of(Helpers.HTMLUNIT)
 
  Before{
    testServer = TestServer(3333)
    testServer.start()
    browser = TestBrowser.of(Helpers.HTMLUNIT)
  }
  After{
    browser.quit()
    testServer.stop()
  }
}