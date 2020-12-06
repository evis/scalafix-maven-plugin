package io.github.evis.scalafix.maven.plugin

import java.io.PrintStream
import java.net.URLClassLoader
import java.nio.charset.Charset
import java.nio.file.{Path, PathMatcher}

import io.github.evis.scalafix.maven.plugin.ScalafixArgumentsBuilder._
import io.github.evis.scalafix.maven.plugin.params._
import scalafix.interfaces.{
  Scalafix,
  ScalafixArguments,
  ScalafixMainCallback,
  ScalafixMainMode
}

import scala.collection.JavaConverters._

/**
  * It has the same methods as [[ScalafixArguments]], but:
  *
  * 1. withXXX methods, which take lists, add to current list. For example:
  *
  * scalafix
  *   .newArguments()
  *   .withScalacOptions(singletonList("1"))
  *   .withScalacOptions(singletonList("2")) // contains only 2
  *
  * ScalafixArgumentsBuilder()
  *   .withScalacOptions(List("1"))
  *   .withScalacOptions(List("2"))
  *   .build(scalafix) // contains both 1 and 2
  *
  * Why: because several mojo params may add elements to the same argument field,
  * e.g., [[CompiledDirectoryParam]] and [[ProjectDependenciesParam]]
  *
  * 2. All withXXX methods take Scala Lists/Options instead of Java for convenience.
  */
final case class ScalafixArgumentsBuilder(
    rules: List[String] = Nil,
    toolClasspath: Option[URLClassLoader] = None,
    paths: List[Path] = Nil,
    excludedPaths: List[PathMatcher] = Nil,
    workingDirectory: Option[Path] = None,
    config: Option[Path] = None,
    mode: Option[ScalafixMainMode] = None,
    parsedArguments: List[String] = Nil,
    printStream: Option[PrintStream] = None,
    classpath: List[Path] = Nil,
    sourceroot: Option[Path] = None,
    mainCallback: Option[ScalafixMainCallback] = None,
    charset: Option[Charset] = None,
    scalaVersion: Option[String] = None,
    scalacOptions: List[String] = Nil
) {

  def build(scalafix: Scalafix): ScalafixArguments = {
    scalafix
      .newArguments()
      .withRules(rules.asJava)
      .withToolClasspath(toolClasspath)
      .withPaths(paths.asJava)
      .withExcludedPaths(excludedPaths.asJava)
      .withWorkingDirectory(workingDirectory)
      .withConfig(config.asJava)
      .withMode(mode)
      .withParsedArguments(parsedArguments.asJava)
      .withPrintStream(printStream)
      .withClasspath(classpath.asJava)
      .withSourceroot(sourceroot)
      .withMainCallback(mainCallback)
      .withCharset(charset)
      .withScalaVersion(scalaVersion)
      .withScalacOptions(scalacOptions.asJava)
  }

  def patch(param: MojoParam): ScalafixArgumentsBuilder = param(this)

  def withRules(rules: List[String]): ScalafixArgumentsBuilder =
    copy(rules = this.rules ::: rules)

  def withToolClasspath(
      toolClasspath: URLClassLoader): ScalafixArgumentsBuilder =
    copy(toolClasspath = Some(toolClasspath))

  def withPaths(paths: List[Path]): ScalafixArgumentsBuilder =
    copy(paths = this.paths ::: paths)

  def withExcludedPaths(
      excludedPaths: List[PathMatcher]): ScalafixArgumentsBuilder =
    copy(excludedPaths = this.excludedPaths ::: excludedPaths)

  def withConfig(config: Path): ScalafixArgumentsBuilder =
    copy(config = Some(config))

  def withMode(mode: ScalafixMainMode): ScalafixArgumentsBuilder =
    copy(mode = Some(mode))

  def withParsedArguments(
      parsedArguments: List[String]): ScalafixArgumentsBuilder =
    copy(parsedArguments = this.parsedArguments ::: parsedArguments)

  def withPrintStream(printStream: PrintStream): ScalafixArgumentsBuilder =
    copy(printStream = Some(printStream))

  def withClasspath(classpath: List[Path]): ScalafixArgumentsBuilder =
    copy(classpath = this.classpath ::: classpath)

  def withSourceroot(sourceroot: Path): ScalafixArgumentsBuilder =
    copy(sourceroot = Some(sourceroot))

  def withMainCallback(
      mainCallback: ScalafixMainCallback): ScalafixArgumentsBuilder =
    copy(mainCallback = Some(mainCallback))

  def withCharset(charset: Charset): ScalafixArgumentsBuilder =
    copy(charset = Some(charset))

  def withScalaVersion(scalaVersion: String): ScalafixArgumentsBuilder =
    copy(scalaVersion = Some(scalaVersion))

  def withScalaVersion(scalaVersion: Option[String]): ScalafixArgumentsBuilder =
    copy(scalaVersion = scalaVersion)

  def withScalacOptions(scalacOptions: List[String]): ScalafixArgumentsBuilder =
    copy(scalacOptions = this.scalacOptions ::: scalacOptions)
}

object ScalafixArgumentsBuilder {

  implicit class ArgumentsOps(private val arguments: ScalafixArguments)
      extends AnyVal {

    def withToolClasspath(
        toolClasspath: Option[URLClassLoader]): ScalafixArguments =
      withOpt(toolClasspath)(_.withToolClasspath)

    def withWorkingDirectory(
        workingDirectory: Option[Path]): ScalafixArguments =
      withOpt(workingDirectory)(_.withWorkingDirectory)

    def withMode(mode: Option[ScalafixMainMode]): ScalafixArguments =
      withOpt(mode)(_.withMode)

    def withPrintStream(printStream: Option[PrintStream]): ScalafixArguments =
      withOpt(printStream)(_.withPrintStream)

    def withSourceroot(sourceroot: Option[Path]): ScalafixArguments =
      withOpt(sourceroot)(_.withSourceroot)

    def withMainCallback(
        mainCallback: Option[ScalafixMainCallback]): ScalafixArguments =
      withOpt(mainCallback)(_.withMainCallback)

    def withCharset(charset: Option[Charset]): ScalafixArguments =
      withOpt(charset)(_.withCharset)

    def withScalaVersion(scalaVersion: Option[String]): ScalafixArguments =
      withOpt(scalaVersion)(_.withScalaVersion)

    private def withOpt[A](value: Option[A])(
        f: ScalafixArguments => A => ScalafixArguments): ScalafixArguments =
      value.fold(arguments)(f(arguments))
  }
}
