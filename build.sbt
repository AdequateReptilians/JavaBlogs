name := """play-java"""version := "1.0-SNAPSHOT"lazy val root = (project in file(".")).enablePlugins(PlayJava)scalaVersion := "2.11.7"libraryDependencies ++= Seq(  javaJdbc,  cache,  javaJpa,  "org.postgresql" % "postgresql" % "9.4.1209.jre7",  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final")fork in run := true

fork in run := true