package io.github.evis.scalafix.maven.plugin

import org.scalatest.{EitherValues, FlatSpec, LoneElement, Matchers, OptionValues}

trait BaseSpec extends FlatSpec with Matchers with LoneElement with OptionValues with EitherValues
