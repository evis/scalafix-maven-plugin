package io.github.evis.scalafix.maven.plugin.phases

import java.io.File

import io.github.evis.scalafix.maven.plugin.BaseSpec
import io.github.evis.scalafix.maven.plugin.log.{LogLevel, TestLog}
import io.github.evis.scalafix.maven.plugin.params.{
  ConfigParam,
  ModeParam,
  SourceDirectoryParam
}
import org.apache.maven.plugin.MojoExecutionException
import scalafix.interfaces.ScalafixMainMode.CHECK

class RunSpec extends BaseSpec with Run {

  "run()" should "succeed if no issues found in project" in {
    val log = new TestLog(LogLevel.Debug)
    val params =
      List(
        SourceDirectoryParam("src/test/resources/project" :: Nil),
        ConfigParam(
          new File("src/test/resources/project/.success.scalafix.conf")),
        ModeParam(CHECK)
      )
    noException should be thrownBy run(params, log)
  }

  it should "fail if there are issues found in project" in {
    val log = new TestLog(LogLevel.Debug)
    val params =
      List(
        SourceDirectoryParam("src/test/resources/project" :: Nil),
        ConfigParam(new File("src/test/resources/project/.fail.scalafix.conf")),
        ModeParam(CHECK)
      )
    a[MojoExecutionException] should be thrownBy run(params, log)
  }
}
