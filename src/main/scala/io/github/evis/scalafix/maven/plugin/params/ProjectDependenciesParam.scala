package io.github.evis.scalafix.maven.plugin.params

import java.util.jar.JarFile
import scala.util.Try
import org.apache.maven.artifact.Artifact
import org.apache.maven.plugin.logging.Log

object ProjectDependenciesParam {

  def apply(projectDependencies: Iterable[Artifact], log: Log): MojoParam = {
    val deps = projectDependencies.toList
    val scala3Library =
      deps.find { dep =>
        dep.getGroupId == "org.scala-lang" && dep.getArtifactId == "scala3-library_3"
      }
    val scalaLibrary =
      deps.find { dep =>
        dep.getGroupId == "org.scala-lang" && dep.getArtifactId == "scala-library"
      }
    (scala3Library, scalaLibrary) match {
      case (Some(scala3), Some(_)) =>
        log.warn(
          s"both scala3-library_3 and scala-library are present in classpath, set Scala version = ${scala3.getVersion}")
      case _ =>
    }
    _.withClasspath(deps.flatMap { dep =>
      /* Some dependencies could be POM files, which scalameta's ClassPathIndex
       * (used by scalafix) would fail to load since it tries to open them using
       * JarFile. Hence, use JarFile ourselves to filter. */
      val file = dep.getFile
      val ok = !file.isFile || Try(new JarFile(file)).isSuccess
      if (ok) Some(file.toPath) else None
    }).withScalaVersion(scala3Library.orElse(scalaLibrary).map(_.getVersion))
  }
}
