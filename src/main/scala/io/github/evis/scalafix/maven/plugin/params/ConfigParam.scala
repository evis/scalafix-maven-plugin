package io.github.evis.scalafix.maven.plugin.params

import java.io.File

object ConfigParam {

  def apply(file: File): MojoParam = {
    // file can be null due to it being mojo parameter without default value.
    // So, handle null case with Option.apply.
    _.copy(config = Option(file).map(_.toPath))
  }
}
