package io.github.evis.scalafix.maven.plugin.phases

import org.apache.maven.plugin.logging.Log

trait LogScalafixArguments {

  def log(logger: Log, arguments: ScalafixArgumentsBuildResult): Unit = {
    logger.debug(s"arguments = $arguments")
  }
}
