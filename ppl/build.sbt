val antlr_jar = "./external/antlr-4.6-complete.jar"

val jasmin_jar = "./external/jasmin.jar"

val extra = if (sys.props("os.name").startsWith("Windows")) "/src/main/mc/parser" else ""

scalaVersion := "2.12.3"

name := "mc"

version := "1.0"

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1"

libraryDependencies += scalatest % Test

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest ,"-fW", "result.txt","-eNDXELO")

lazy val gen = TaskKey[Int]("Antlr generates lexer and parser")

lazy val grun = TaskKey[Unit]("Draw parse tree")

javaSource in Compile := (sourceManaged in Compile).value / "src" / "main"

scalaSource in Compile := baseDirectory.value / "src" / "main"

unmanagedJars in Compile ++= Seq(file(antlr_jar),file(jasmin_jar))

unmanagedJars in Test += file(jasmin_jar)

lazy val root = (project in file("."))
  .settings(
    gen := { val exitCode = scala.sys.process.Process("java",Seq("-cp", antlr_jar, "org.antlr.v4.Tool", "-o",((sourceManaged in Compile).value).getPath + extra, "-no-listener", "-visitor", "src/main/mc/parser/MC.g4")).! 
    			if(exitCode != 0) sys.error(s"Antlr4 failed with exit code $exitCode") 
    			exitCode
    		  },
    grun := {
    	val exitCode = scala.sys.process.Process("java",Seq("-cp", antlr_jar, "org.antlr.v4.gui.TestRig")).!
    	if(exitCode != 0) sys.error(s"Antlr4 failed with exit code $exitCode")
    }
 )

compile := ((compile in Compile) dependsOn gen).value
