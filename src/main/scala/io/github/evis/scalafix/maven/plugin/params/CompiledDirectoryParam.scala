package io.github.evis.scalafix.maven.plugin.params

import java.nio.file.Paths

object CompiledDirectoryParam {

  def apply(dirs: List[String]): MojoParam = _.withClasspath {
    dirs.map(Paths.get(_)).filter(_.toFile.exists)
  }

  def apply(dirs: String*): MojoParam = apply(dirs.toList)

}
