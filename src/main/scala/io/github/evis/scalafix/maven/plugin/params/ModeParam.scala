package io.github.evis.scalafix.maven.plugin.params

import scalafix.interfaces.ScalafixMainMode

object ModeParam {

  def apply(mode: ScalafixMainMode): MojoParam = {
    _.withMode(mode)
  }
}
