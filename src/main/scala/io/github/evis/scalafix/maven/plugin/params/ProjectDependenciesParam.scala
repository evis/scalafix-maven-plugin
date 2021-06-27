package io.github.evis.scalafix.maven.plugin.params

import org.apache.maven.artifact.Artifact

object ProjectDependenciesParam {

  def apply(projectDependencies: Iterable[Artifact]): MojoParam = {
    val deps = projectDependencies.toList
    val scalaLibrary =
      deps.find { dep =>
        dep.getGroupId == "org.scala-lang" && dep.getArtifactId == "scala-library"
      }
    _.withClasspath(deps.map(_.getFile.toPath))
      .withScalaVersion(scalaLibrary.map(_.getVersion))
  }
}
