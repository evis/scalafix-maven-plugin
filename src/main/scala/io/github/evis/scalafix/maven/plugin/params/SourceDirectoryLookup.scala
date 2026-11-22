package io.github.evis.scalafix.maven.plugin.params

import java.io.File
import java.nio.file.Path
import java.util.{List => JList}

import scala.collection.JavaConverters._

import org.apache.maven.project.MavenProject

class SourceDirectoryLookup(fileOps: FileOps, project: MavenProject) {

  private val build = project.getBuild()
  private val outpath = getCanonicalFile(build.getDirectory())

  def getMain(customDirs: Iterable[File]): List[Path] =
    getFiles(
      customDirs,
      build.getSourceDirectory(),
      project.getCompileSourceRoots()
    )

  def getTest(customDirs: Iterable[File]): List[Path] =
    getFiles(
      customDirs,
      build.getTestSourceDirectory(),
      project.getTestCompileSourceRoots()
    )

  private def getFiles(
      customDirs: Iterable[File],
      primaryDir: => String,
      alternativeDirs: => JList[String]
  ): List[Path] = {
    val customDirsOpt = Option(customDirs).filter(_.nonEmpty)
    customDirsOpt.fold {
      val builder = Set.newBuilder[Path]
      builder += getCanonicalFile(primaryDir + "/../scala")
      builder ++= alternativeDirs.asScala
        .map(getCanonicalFile)
        .filter(!_.startsWith(outpath))
      // check for existence only for implicit sources
      builder.result().filter(x => fileOps.exists(x.toFile)).toList
    }(_.map(getCanonicalFile).toList.distinct)
  }

  // Usually we execute plugin on source directory with path either like
  // src/{main,test}/java/../scala or src/{main,test}/scala/../scala. Anyway,
  // this path is relative, not canonical. Scalafix throws MissingTextDocument
  // exception when tries to find sources by relative path. As a workaround, we
  // provide canonical path for Scalafix.

  private def getCanonicalFile(dir: String): Path =
    normalize(project.getBasedir().toPath().resolve(dir))

  private def getCanonicalFile(dir: File): Path =
    normalize(project.getBasedir().toPath().resolve(dir.toPath))

  private def normalize(dir: Path): Path =
    dir.toFile().getCanonicalFile().toPath()

}
