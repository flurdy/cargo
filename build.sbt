name := """cargo"""

version := "0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= {
   val enumeratumVersion = "1.5.8"
   Seq(
      //  jdbc,
      //  evolutions
      "com.beachape" %% "enumeratum" % enumeratumVersion,
      "com.beachape" %% "enumeratum-play" % enumeratumVersion,
      "com.beachape" %% "enumeratum-play-json" % enumeratumVersion,
      "org.webjars" %% "webjars-play" % "2.5.0",
      "org.webjars" % "bootstrap" % "3.3.6",
      "org.webjars" % "jquery" % "2.2.3",
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
   )
}
