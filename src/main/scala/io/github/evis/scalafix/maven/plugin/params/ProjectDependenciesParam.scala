package io.github.evis.scalafix.maven.plugin.params

import org.apache.maven.artifact.Artifact

import scala.collection.JavaConverters._

object ProjectDependenciesParam {

  def apply(projectDependencies: java.util.Set[Artifact]): MojoParam = {
    val deps = projectDependencies.asScala.toList
    val scalaLibrary =
      deps.find { dep =>
        dep.getGroupId == "org.scala-lang" && dep.getArtifactId == "scala-library"
      }
    _.withClasspath(deps.map(_.getFile.toPath))
      .withScalaVersion(scalaLibrary.map(_.getVersion))
  }
}
