package io.github.evis.scalafix.maven.plugin

import org.scalatest.{
  EitherValues,
  LoneElement,
  OptionValues
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait BaseSpec
    extends AnyFlatSpec
    with Matchers
    with LoneElement
    with OptionValues
    with EitherValues
