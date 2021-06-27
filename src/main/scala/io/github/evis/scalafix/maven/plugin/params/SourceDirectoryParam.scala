package io.github.evis.scalafix.maven.plugin.params

import java.nio.file.Paths

object SourceDirectoryParam {

  def apply(dirs: List[String]): MojoParam = _.withPaths {
    dirs.map(getCanonicalPath).filter(_.toFile.exists)
  }

  // Usually we execute plugin on source directory with path either like
  // src/{main,test}/java/../scala or src/{main,test}/scala/../scala. Anyway,
  // this path is relative, not canonical. Scalafix throws MissingTextDocument
  // exception when tries to find sources by relative path. As a workaround, we
  // provide canonical path for Scalafix.
  private def getCanonicalPath(directory: String) =
    Paths.get(directory).toFile.getCanonicalFile.toPath
}
