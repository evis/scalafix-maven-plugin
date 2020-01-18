package io.github.evis.scalafix.maven.plugin.params

import java.nio.file.Paths
import scala.collection.JavaConverters._

object SourceDirectoryParam {

  def apply(
      sourceDirectory: String,
      sourceDirectories: java.util.List[String]): MojoParam = {
    val dirs = Option(sourceDirectories).map(_.asScala).getOrElse(Nil)

    val paths = (sourceDirectory +: dirs.toList)
      .map(getCanonicalPath)
      .distinct
      .filter(_.toFile.exists)

    _.withPaths(paths)
  }

  def apply(sourceDirectory: String): MojoParam = {
    _.withPaths(List(getCanonicalPath(sourceDirectory)).filter(_.toFile.exists))
  }

  // Usually we execute plugin on source directory with path either like
  // src/{main,test}/java/../scala or src/{main,test}/scala/../scala. Anyway,
  // this path is relative, not canonical. Scalafix throws MissingTextDocument
  // exception when tries to find sources by relative path. As a workaround, we
  // provide canonical path for Scalafix.
  private def getCanonicalPath(directory: String) =
    Paths.get(directory).toFile.getCanonicalFile.toPath
}
