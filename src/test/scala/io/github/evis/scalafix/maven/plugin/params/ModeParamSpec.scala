package io.github.evis.scalafix.maven.plugin.params

import scalafix.interfaces.ScalafixMainMode._

class ModeParamSpec extends ParamSpec {

  "ModeParam" should "set IN_PLACE mode" in {
    ModeParam(IN_PLACE).applied.mode.value shouldBe IN_PLACE
  }

  it should "set CHECK mode" in {
    ModeParam(CHECK).applied.mode.value shouldBe CHECK
  }

  it should "set STDOUT mode" in {
    ModeParam(STDOUT).applied.mode.value shouldBe STDOUT
  }

  it should "set AUTO_SUPPRESS_LINTER_ERRORS mode" in {
    ModeParam(
      AUTO_SUPPRESS_LINTER_ERRORS
    ).applied.mode.value shouldBe AUTO_SUPPRESS_LINTER_ERRORS
  }
}
