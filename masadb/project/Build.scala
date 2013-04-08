import sbt._
import Keys._
import play.Project._
import templemore.xsbt.cucumber.CucumberPlugin

object ApplicationBuild extends Build {

  val appName         = "MasaDB"
  val appVersion      = "0.1-SNAPSHOT"

  val buildSettings = Defaults.defaultSettings ++
                      CucumberPlugin.cucumberSettings ++
                      Seq ( CucumberPlugin.cucumberFeaturesDir := file("./test/features/"),
                            CucumberPlugin.cucumberJunitReport := true,
                            CucumberPlugin.cucumberHtmlReport := true)

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies, settings = buildSettings).settings(
    // Add your own project settings here      
  )

}
