package io.github.evis.scalafix.maven.plugin.params

import io.github.evis.scalafix.maven.plugin.{BaseSpec, ScalafixArgumentsBuilder}

trait ParamSpec extends BaseSpec {

  implicit class MojoParamOps(private val param: MojoParam) {

    def applied: ScalafixArgumentsBuilder =
      ScalafixArgumentsBuilder().patch(param)
  }

}
