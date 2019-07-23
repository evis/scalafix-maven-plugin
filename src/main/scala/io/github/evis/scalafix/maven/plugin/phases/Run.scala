package io.github.evis.scalafix.maven.plugin.phases

import io.github.evis.scalafix.maven.plugin.params.MojoParams
import org.apache.maven.plugin.logging.Log

trait Run
    extends LoadScalafix
    with BuildScalafixArguments
    with LogScalafixArguments
    with ShowErrors {

  def run(logger: Log, params: MojoParams): Unit = {
    val scalafix = loadScalafix()
    val arguments = buildScalafixArguments(scalafix, params)
    log(logger, arguments)
    val errors = arguments.map(_.run().toList).getOrElse(Nil)
    showErrors(errors)
  }
}
