package io.github.evis.scalafix.maven.plugin.params

import java.util.jar.JarFile

import scala.util.Try

import org.apache.maven.artifact.Artifact

object ProjectDependenciesParam {

  def apply(projectDependencies: Iterable[Artifact]): MojoParam = {
    val deps = projectDependencies.toList
    val scalaLibrary =
      deps.find { dep =>
        dep.getGroupId == "org.scala-lang" && (dep.getArtifactId == "scala-library" || dep.getArtifactId == "scala3-library_3")
      }
    _.withClasspath(deps.flatMap { dep =>
      /* Some dependencies could be POM files, which scalameta's ClassPathIndex
       * (used by scalafix) would fail to load since it tries to open them using
       * JarFile. Hence, use JarFile ourselves to filter. */
      val file = dep.getFile
      val ok = !file.isFile || Try(new JarFile(file)).isSuccess
      if (ok) Some(file.toPath) else None
    }).withScalaVersion(scalaLibrary.map(_.getVersion))
  }
}
