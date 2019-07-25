package io.github.evis.scalafix.maven.plugin.phases

import org.apache.maven.plugin.MojoExecutionException
import scalafix.interfaces.ScalafixError

trait ShowErrors {

  @SuppressWarnings(Array("scalafix:DisableSyntax.throw"))
  def showErrors(errors: List[ScalafixError]): Unit = {
    if (errors.nonEmpty) {
      throw new MojoExecutionException(
        "Scalafix invoked with errors. Check logs for details.")
    }
  }
}
