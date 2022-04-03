package io.github.evis.scalafix.maven.plugin.phases

import io.github.evis.scalafix.maven.plugin.BaseSpec
import io.github.evis.scalafix.maven.plugin.ScalafixArgumentsBuildError.EmptyPaths
import io.github.evis.scalafix.maven.plugin.params.SourceDirectoryParam
import scalafix.internal.interfaces.{ScalafixArgumentsImpl, ScalafixImpl}

@SuppressWarnings(Array("scalafix:DisableSyntax.asInstanceOf"))
class BuildScalafixArgumentsSpec extends BaseSpec with BuildScalafixArguments {

  "buildScalafixArguments()" should "build if source directory is given" in {
    val scalafix = new ScalafixImpl
    val params = List(SourceDirectoryParam("src/main/scala"))
    val res = buildScalafixArguments(scalafix, params)
    res shouldBe a[Right[_, _]]
    res.right.value
      .asInstanceOf[ScalafixArgumentsImpl]
      .args
      .files
      .loneElement
      .toRelative
      .toString shouldBe "src/main/scala"
  }

  it should "not check if source directory doesn't exist" in {
    val scalafix = new ScalafixImpl
    val params = List(SourceDirectoryParam("src/main/unexisting"))
    val res = buildScalafixArguments(scalafix, params)
    res shouldBe a[Right[_, _]]
    res.right.value
      .asInstanceOf[ScalafixArgumentsImpl]
      .args
      .files
      .loneElement
      .toRelative
      .toString shouldBe "src/main/unexisting"
  }

  it should "not build if source directory isn't given" in {
    val scalafix = new ScalafixImpl
    val res = buildScalafixArguments(scalafix, params = Nil)
    res shouldBe a[Left[_, _]]
    res.left.value shouldBe EmptyPaths
  }
}
