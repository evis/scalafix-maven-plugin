package io.github.evis.scalafix.maven.plugin.params

class CommandLineArgsParamSpec extends ParamSpec {

  "CommandLineArgsParam" should "add Scalafix command line arguments" in {
    val args = new java.util.ArrayList[String]()
    args.add("--syntactic")
    args.add("--verbose")
    CommandLineArgsParam(args).applied.parsedArguments shouldBe List(
      "--syntactic",
      "--verbose")
  }
}
