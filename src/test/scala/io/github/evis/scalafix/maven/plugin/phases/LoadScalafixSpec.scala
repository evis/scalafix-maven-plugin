package io.github.evis.scalafix.maven.plugin.phases

import io.github.evis.scalafix.maven.plugin.BaseSpec

class LoadScalafixSpec extends BaseSpec with LoadScalafix {

  "loadScalafix()" should "not throw" in {
    noException should be thrownBy loadScalafix()
  }

  it should "not return null" in {
    loadScalafix() should not be null
  }
}
