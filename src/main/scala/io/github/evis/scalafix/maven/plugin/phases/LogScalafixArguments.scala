package io.github.evis.scalafix.maven.plugin.phases

import org.apache.maven.plugin.Mojo

trait LogScalafixArguments {

  def log(mojo: Mojo, arguments: ScalafixArgumentsBuildResult): Unit = {
    mojo.getLog.debug(s"arguments = $arguments")
  }
}
