import com.gu.deploy.PlayArtifact._
import play.PlayImport.PlayKeys._
import play.PlayImport._
import sbt._
import sbt.Keys._
import com.gu.riffraff.artifact._
import RiffRaffArtifact.autoImport._
import com.typesafe.sbt.packager.Keys._

object R2ArchiveViewApp extends Build {


  val libDependencies = Seq(
    jdbc,
    "com.amazonaws" % "aws-java-sdk" % "1.9.23",
    "net.logstash.logback" % "logstash-logback-encoder" % "4.2",
    "javax.persistence" % "persistence-api" % "1.0")


  val commonSettings = Seq(
    scalaVersion := "2.11.7",
    scalaVersion in ThisBuild := "2.11.7",
    organization := "com.gu",
    version      := "0.1",
    resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"),
    scalacOptions ++= Seq("-feature", "-deprecation", "-language:higherKinds", "-Xfatal-warnings"),
    doc in Compile <<= target.map(_ / "none"),
    incOptions := incOptions.value.withNameHashing(nameHashing = true)
  )
  val projectName = "r2-archive-view"
  lazy val root =
    Project(projectName, file("."))
      .enablePlugins(play.PlayScala)
      .enablePlugins(RiffRaffArtifact)
      .settings(commonSettings ++ playArtifactDistSettings :_*)
      .settings(libraryDependencies  ++= libDependencies)
      .settings(magentaPackageName := projectName)
      .settings(playDefaultPort := 9301)
      .settings(
        riffRaffPackageType := (packageZipTarball in config("universal")).value,
        riffRaffPackageName := name.value,
        riffRaffManifestProjectName := s"R2:${name.value}",
        riffRaffBuildIdentifier := Option(System.getenv("CIRCLE_BUILD_NUM")).getOrElse("DEV"),
        riffRaffUploadArtifactBucket := Option("riffraff-artifact"),
        riffRaffUploadManifestBucket := Option("riffraff-builds")
      )



}