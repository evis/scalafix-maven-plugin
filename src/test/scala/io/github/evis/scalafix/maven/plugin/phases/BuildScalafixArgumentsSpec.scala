package io.github.evis.scalafix.maven.plugin.phases

import io.github.evis.scalafix.maven.plugin.BaseSpec
import io.github.evis.scalafix.maven.plugin.ScalafixArgumentsBuildError.EmptyPaths
import io.github.evis.scalafix.maven.plugin.params.SourceDirectoryParam
import scalafix.internal.interfaces.{ScalafixArgumentsImpl, ScalafixImpl}

@SuppressWarnings(Array("scalafix:DisableSyntax.asInstanceOf"))
class BuildScalafixArgumentsSpec extends BaseSpec with BuildScalafixArguments {

  "buildScalafixArguments()" should "build if source directory is given" in {
    val scalafix = new ScalafixImpl
    val params = List(SourceDirectoryParam("src/main/scala" :: Nil))
    val arguments = buildScalafixArguments(scalafix, params).right.value
    arguments
      .asInstanceOf[ScalafixArgumentsImpl]
      .args
      .files
      .loneElement
      .toRelative
      .toString shouldBe "src/main/scala"
  }

  it should "not build if source directory doesn't exist" in {
    val scalafix = new ScalafixImpl
    val params = List(SourceDirectoryParam("src/main/unexisting" :: Nil))
    buildScalafixArguments(scalafix, params).left.value shouldBe EmptyPaths
  }

  it should "not build if source directory isn't given" in {
    val scalafix = new ScalafixImpl
    buildScalafixArguments(scalafix, params = Nil).left.value shouldBe EmptyPaths
  }
}
