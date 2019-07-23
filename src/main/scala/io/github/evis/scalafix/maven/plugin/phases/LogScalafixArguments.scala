package io.github.evis.scalafix.maven.plugin.phases

import org.apache.maven.plugin.logging.Log

trait LogScalafixArguments {

  implicit class LogOps(private val logger: Log) {

    def log(arguments: ScalafixArgumentsBuildResult): Unit = {
      logger.debug(s"arguments = $arguments")
    }
  }
}
