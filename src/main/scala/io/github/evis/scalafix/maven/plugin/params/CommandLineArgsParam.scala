package io.github.evis.scalafix.maven.plugin.params

import scala.collection.JavaConverters._

object CommandLineArgsParam {

  def apply(args: java.util.List[String]): MojoParam = {
    _.withParsedArguments(args.asScala.toList)
  }
}
