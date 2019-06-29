package io.github.evis.scalafix.maven.plugin.params

import java.nio.file.Paths

object CompiledDirectoryParam {

  def apply(compiledDirectory: String): MojoParam = {
    _.withClasspath(List(Paths.get(compiledDirectory)).filter(_.toFile.exists))
  }
}
