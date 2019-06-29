package io.github.evis.scalafix.maven.plugin.phases

import org.apache.maven.plugin.{Mojo, MojoExecutionException}
import scalafix.interfaces.ScalafixError

trait ShowErrors {

  def showErrors(mojo: Mojo, errors: List[ScalafixError]): Unit = {
    if (errors.nonEmpty) {
      throw new MojoExecutionException(
        "Scalafix invoked with errors. Check logs for details.")
    }
  }
}
