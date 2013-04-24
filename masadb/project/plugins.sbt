// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1.0")

resolvers += "Templemore repository" at "http://templemore.co.uk/repo"

addSbtPlugin("templemore" % "xsbt-cucumber-plugin" % "0.6.2")

// JaCoCo. (2.0.0 ei toimi play 2.1:n kanssa!)
addSbtPlugin("de.johoop" % "jacoco4sbt" % "1.2.4")

// War deploy plugin
addSbtPlugin("com.github.play2war" % "play2-war-plugin" % "0.9")