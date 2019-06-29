package io.github.evis.scalafix.maven.plugin

import scalafix.interfaces.ScalafixArguments

package object phases {

  type ScalafixArgumentsBuildResult =
    Either[ScalafixArgumentsBuildError, ScalafixArguments]
}
