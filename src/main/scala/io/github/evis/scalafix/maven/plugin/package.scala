package io.github.evis.scalafix.maven

import java.util.Optional

package object plugin {

  implicit class OptionOps[A](private val option: Option[A]) extends AnyVal {

    def asJava: Optional[A] = option match {
      case Some(x) =>
        Optional.of(x)
      case None =>
        Optional.empty[A]
    }
  }
}
