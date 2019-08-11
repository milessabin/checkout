inThisBuild(Seq(
  organization := "com.milessabin",
  scalaVersion := "2.13.0"
))

lazy val root = (project in file("."))
  .settings(
    name := "checkout",
    scalacOptions := Seq(
      "-feature",
      "-Xfatal-warnings",
      "-deprecation",
      "-unchecked",
    ),
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest"  % "3.0.8"  % "test",
      "org.scalacheck" %% "scalacheck" % "1.14.0" % "test" 
    )
  )
