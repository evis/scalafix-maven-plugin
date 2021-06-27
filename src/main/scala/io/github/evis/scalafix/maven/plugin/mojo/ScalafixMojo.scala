package io.github.evis.scalafix.maven.plugin.mojo

import java.io.File

import io.github.evis.scalafix.maven.plugin.params._
import org.apache.maven.artifact.Artifact
import org.apache.maven.model.Plugin
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.{Mojo, Parameter, ResolutionScope}

import scala.collection.JavaConverters._
import scalafix.interfaces.ScalafixMainMode

//noinspection VarCouldBeVal
// Require dependency resolution to avoid nulls from artifact.getFile
// in ProjectDependenciesParam.
// Test dependency scope should include all dependencies from compile scope.
@Mojo(name = "scalafix", requiresDependencyResolution = ResolutionScope.TEST)
@SuppressWarnings(Array("scalafix:DisableSyntax.var"))
final class ScalafixMojo extends AbstractMojo {

  // If java source directory exists in project, then
  // project.build.sourceDirectory points to it. So, we go to ../scala for
  // Scala sources. scala-maven-plugin does the same.
  // (keep one empty line after comment to avoid removal of the comment by
  // scalafix)

  @Parameter(
    defaultValue = "${project.build.sourceDirectory}/../scala",
    required = true,
    readonly = true)
  private var sourceDirectory: String = _

  @Parameter(
    defaultValue = "${project.build.testSourceDirectory}/../scala",
    required = true,
    readonly = true)
  private var testSourceDirectory: String = _

  @Parameter(
    defaultValue = "${project.artifacts}",
    required = true,
    readonly = true)
  private var projectDependencies: java.util.Set[Artifact] = _

  @Parameter(
    defaultValue = "${project.build.outputDirectory}",
    required = true,
    readonly = true)
  private var compiledDirectory: String = _

  @Parameter(
    defaultValue = "${project.build.testOutputDirectory}",
    required = true,
    readonly = true)
  private var testCompiledDirectory: String = _

  @Parameter(
    defaultValue = "${project.build.plugins}",
    required = true,
    readonly = true)
  private var plugins: java.util.List[Plugin] = _

  @Parameter(property = "scalafix.mode", defaultValue = "IN_PLACE")
  private var mode: ScalafixMainMode = _

  @Parameter(property = "scalafix.command.line.args")
  private var commandLineArgs: java.util.List[String] = _

  @Parameter(property = "scalafix.skip", defaultValue = "false")
  private var skip: Boolean = _

  @Parameter(property = "scalafix.skip.main", defaultValue = "false")
  private var skipMain: Boolean = _

  @Parameter(property = "scalafix.skip.test", defaultValue = "false")
  private var skipTest: Boolean = _

  @Parameter(property = "scalafix.config")
  private var config: File = _

  override def execute(): Unit = {
    if (skip) {
      getLog.info("Skip scalafix since skip flag passed")
    } else if (skipMain && skipTest) {
      getLog.info(
        "Skip scalafix since both skip.main and skip.test flags passed")
    } else {
      val mainSources = getSources(
        skipMain,
        "main",
        sourceDirectory
      )
      val testSources = getSources(
        skipTest,
        "test",
        testSourceDirectory
      )
      val mainOutputs = if (skipMain) Seq.empty else Seq(compiledDirectory)
      val testOutputs = if (skipTest) Seq.empty else Seq(testCompiledDirectory)
      val params =
        List(
          SourceDirectoryParam(mainSources ++ testSources),
          ProjectDependenciesParam(projectDependencies.asScala),
          CompiledDirectoryParam(mainOutputs ++ testOutputs),
          PluginsParam(plugins.asScala),
          ModeParam(mode),
          ConfigParam(config),
          CommandLineArgsParam(commandLineArgs)
        )
      run(params, getLog)
    }
  }

  private def getSources(
      flag: Boolean,
      flagName: String,
      projectDir: String): Iterable[File] = {
    val dirs =
      if (flag) Seq.empty
      else
        new File(projectDir, "/../scala") :: Nil
    if (dirs.isEmpty) getLog.info(s"Skip scalafix[$flagName]")
    else dirs.foreach(dir => getLog.info(s"Processing[$flagName]: $dir"))
    dirs
  }
}
