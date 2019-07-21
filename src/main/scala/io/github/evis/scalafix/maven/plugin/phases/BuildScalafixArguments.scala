package io.github.evis.scalafix.maven.plugin.phases

import io.github.evis.scalafix.maven.plugin.ScalafixArgumentsBuildError.EmptyPaths
import io.github.evis.scalafix.maven.plugin.params._
import scalafix.interfaces.Scalafix

trait BuildScalafixArguments {

  def buildScalafixArguments(
      scalafix: Scalafix,
      params: MojoParams
  ): ScalafixArgumentsBuildResult = {
    val builder = params.applied
    // When paths aren't given, Scalafix runs on full given sourceroot, or just
    // on current directory (`.`) if sourceroot isn't given too. That's not
    // what we want most of the time. For example, parent Maven project may
    // not contain Scala sources at all, but Scalafix will find subprojects
    // sources with wrong paths. If one wants to run Scalafix via plugin on
    // `.`, one must do it explicitly by passing `.` as source path. But if
    // paths aren't given, plugin just doesn't create Scalafix arguments
    // (hence, doesn't invoke Scalafix at all).
    if (builder.paths.nonEmpty) {
      Right(builder.build(scalafix))
    } else {
      Left(EmptyPaths)
    }
  }
}
