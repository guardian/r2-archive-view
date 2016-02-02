import com.gu.deploy.PlayArtifact._
import play.PlayImport.PlayKeys._
import play.PlayImport._
import sbt._
import sbt.Keys._

object R2ArchiveViewApp extends Build {


  val libDependencies = Seq(
    jdbc,
    "com.amazonaws" % "aws-java-sdk" % "1.9.23",
    "net.logstash.logback" % "logstash-logback-encoder" % "4.2")


  val commonSettings = Seq(
    scalaVersion := "2.11.1",
    scalaVersion in ThisBuild := "2.11.1",
    organization := "com.gu",
    version      := "0.1",
    resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"),
    scalacOptions ++= Seq("-feature", "-deprecation", "-language:higherKinds", "-Xfatal-warnings"),
    doc in Compile <<= target.map(_ / "none"),
    incOptions := incOptions.value.withNameHashing(nameHashing = true)
  )
  val projectName = "r2-archive-view-app"
  lazy val root = Project(projectName, file(".")).enablePlugins(play.PlayScala)
    .settings(commonSettings ++ playArtifactDistSettings :_*)
    .settings(libraryDependencies  ++= libDependencies)
    .settings(magentaPackageName := projectName)
    .settings(playDefaultPort := 9301)
}