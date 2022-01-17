package io.github.evis.scalafix.maven.plugin.params

import java.nio.file.Path

object SourceDirectoryParam extends SourceDirectoryParam(FileOps)

class SourceDirectoryParam(fileOps: FileOps) {

  def apply(dirs: List[Path]): MojoParam = _.withPaths(dirs)
  def apply(dirs: String*): MojoParam = apply(dirs.map(fileOps.getPath).toList)

}
