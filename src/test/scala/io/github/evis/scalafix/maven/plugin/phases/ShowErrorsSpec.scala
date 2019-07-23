package io.github.evis.scalafix.maven.plugin.phases

import io.github.evis.scalafix.maven.plugin.BaseSpec
import org.apache.maven.plugin.MojoExecutionException
import scalafix.interfaces.ScalafixError.ParseError

class ShowErrorsSpec extends BaseSpec with ShowErrors {

  "showErrors()" should "not throw if no errors" in {
    noException should be thrownBy showErrors(errors = Nil)
  }

  it should "throw MojoExecutionException on error" in {
    a[MojoExecutionException] should be thrownBy showErrors(List(ParseError))
  }
}
