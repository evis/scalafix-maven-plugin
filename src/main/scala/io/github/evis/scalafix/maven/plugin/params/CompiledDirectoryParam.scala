package io.github.evis.scalafix.maven.plugin.params

object CompiledDirectoryParam {

  def apply(dirs: Iterable[String]): MojoParam = {
    _.withClasspath(dirs.map(getPath).filter(_.toFile.exists).toList)
  }

  def apply(dirs: String*): MojoParam = apply(dirs)

}
