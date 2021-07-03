package io.github.evis.scalafix.maven.plugin.params

import java.io.File

object SourceDirectoryParam {

  def apply(dirs: Iterable[File]): MojoParam = {
    _.withPaths(dirs.flatMap(getCanonicalPath).toList.distinct)
  }

  def apply(dirs: String*): MojoParam = apply(dirs.map(getFile))

  // Usually we execute plugin on source directory with path either like
  // src/{main,test}/java/../scala or src/{main,test}/scala/../scala. Anyway,
  // this path is relative, not canonical. Scalafix throws MissingTextDocument
  // exception when tries to find sources by relative path. As a workaround, we
  // provide canonical path for Scalafix.
  private def getCanonicalPath(directory: File) =
    if (directory.exists()) Some(directory.getCanonicalFile.toPath) else None

}
