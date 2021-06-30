package io.github.evis.scalafix.maven.plugin.params

object CompiledDirectoryParam extends CompiledDirectoryParam(FileOps)

class CompiledDirectoryParam(fileOps: FileOps) {

  def apply(dirs: List[String]): MojoParam = _.withClasspath {
    dirs.map(fileOps.getPath).filter(x => fileOps.exists(x.toFile))
  }

  def apply(dirs: String*): MojoParam = apply(dirs.toList)

}
