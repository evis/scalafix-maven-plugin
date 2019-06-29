package io.github.evis.scalafix.maven.plugin

sealed abstract class ScalafixArgumentsBuildError

object ScalafixArgumentsBuildError {

  case object EmptyPaths extends ScalafixArgumentsBuildError
}
