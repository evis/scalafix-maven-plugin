package io.github.evis.scalafix.maven.plugin

package object params {

  type MojoParam = ScalafixArgumentsBuilder => ScalafixArgumentsBuilder

  implicit class MojoParamOps(private val param: MojoParam) extends AnyVal {

    def ifNot(flag: Boolean): MojoParam =
      if (flag) identity else param
  }

  type MojoParams = List[MojoParam]
}
