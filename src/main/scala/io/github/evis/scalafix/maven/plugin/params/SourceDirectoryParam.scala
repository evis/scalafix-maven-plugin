package io.github.evis.scalafix.maven.plugin.params

object SourceDirectoryParam extends SourceDirectoryParam(FileOps)

class SourceDirectoryParam(fileOps: FileOps) {

  def apply(dirs: List[String]): MojoParam = _.withPaths {
    dirs.map(getCanonicalPath).filter(x => fileOps.exists(x.toFile))
  }

  // Usually we execute plugin on source directory with path either like
  // src/{main,test}/java/../scala or src/{main,test}/scala/../scala. Anyway,
  // this path is relative, not canonical. Scalafix throws MissingTextDocument
  // exception when tries to find sources by relative path. As a workaround, we
  // provide canonical path for Scalafix.
  private def getCanonicalPath(directory: String) =
    fileOps.getPath(directory).toFile.getCanonicalFile.toPath
}
