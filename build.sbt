name := "User Management"
version := "1.0"
scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.14",
  "com.typesafe.akka" %% "akka-http" % "10.2.4",
  "com.typesafe.akka" %% "akka-stream" % "2.6.14",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.4",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "mysql" % "mysql-connector-java" % "8.0.25",
  "org.flywaydb" % "flyway-core" % "3.2.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.20",
  "org.scalatest" %% "scalatest" % "3.2.6" ,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.7"


)
