package io.github.evis.scalafix.maven.plugin

import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}

trait BaseSpec extends FlatSpec with Matchers with LoneElement with OptionValues
