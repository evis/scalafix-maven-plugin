package io.github.evis.scalafix.maven.plugin

import java.nio.file.Paths

package object params {

  type MojoParam = ScalafixArgumentsBuilder => ScalafixArgumentsBuilder

  implicit class MojoParamOps(private val param: MojoParam) extends AnyVal {

    def ifNot(flag: Boolean): MojoParam =
      if (flag) identity else param
  }

  type MojoParams = List[MojoParam]

  implicit class MojoParamsOps(private val params: MojoParams) {

    def applied: ScalafixArgumentsBuilder =
      params.foldLeft(ScalafixArgumentsBuilder())(_.patch(_))
  }

  def getPath(path: String) = Paths.get(path)

  def getFile(path: String) = getPath(path).toFile

}
