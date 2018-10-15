name := """cargo-web"""
organization := "com.flurdy"

version := "0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

resolvers += Resolver.bintrayRepo("flurdy", "sander")
resolvers += Resolver.bintrayRepo("flurdy", "scalasoup")

libraryDependencies ++= {
   val enumeratumVersion = "1.5.12"
   Seq(
      guice,
      "com.beachape" %% "enumeratum" % enumeratumVersion,
      "com.beachape" %% "enumeratum-play" % enumeratumVersion,
      "com.beachape" %% "enumeratum-play-json" % enumeratumVersion,
      "com.flurdy" % "sander-core_2.11" % "0.2.0",
      "org.webjars" %% "webjars-play" % "2.6.3",
      "org.webjars" % "bootstrap" % "3.3.7-1",
      "org.webjars" % "jquery" % "3.2.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
      "com.flurdy" %% "scalasoup" % "0.1.0" % Test,
      "org.mockito" % "mockito-core" % "2.13.0" % Test
   )
}

javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

routesImport += "play.api.mvc.PathBindable.bindableUUID"
