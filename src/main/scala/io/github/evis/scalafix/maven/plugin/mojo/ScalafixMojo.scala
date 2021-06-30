package io.github.evis.scalafix.maven.plugin.mojo

import java.io.File
import java.nio.file.Path
import java.util.{List => JList}

import io.github.evis.scalafix.maven.plugin.params._
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.{Mojo, Parameter, ResolutionScope}
import org.apache.maven.project.MavenProject

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

  @Parameter(property = "scalafix.mainSourceDirectories")
  private var mainSourceDirectories: JList[File] = _

  @Parameter(property = "scalafix.testSourceDirectories")
  private var testSourceDirectories: JList[File] = _

  @Parameter(required = true, readonly = true, defaultValue = "${project}")
  private var project: MavenProject = _

  @Parameter(property = "scalafix.mode", defaultValue = "IN_PLACE")
  private var mode: ScalafixMainMode = _

  @Parameter(property = "scalafix.command.line.args")
  private var commandLineArgs: JList[String] = _

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
      val build = project.getBuild
      val buildPath = getPath(build.getDirectory)
      val mainSources = getSources(
        skipMain,
        "main",
        buildPath,
        mainSourceDirectories,
        project.getCompileSourceRoots,
        build.getSourceDirectory
      )
      val testSources = getSources(
        skipTest,
        "test",
        buildPath,
        testSourceDirectories,
        project.getTestCompileSourceRoots,
        build.getTestSourceDirectory
      )
      val mainOutputs =
        if (skipMain) Seq.empty else Seq(build.getOutputDirectory)
      val testOutputs =
        if (skipTest) Seq.empty else Seq(build.getTestOutputDirectory)
      val params =
        List(
          SourceDirectoryParam(mainSources ++ testSources),
          ProjectDependenciesParam(project.getArtifacts.asScala),
          CompiledDirectoryParam(mainOutputs ++ testOutputs),
          PluginsParam(build.getPlugins.asScala),
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
      outputDir: Path,
      customDirs: JList[File],
      projectDirs: JList[String],
      projectDir: String): Iterable[File] = {
    val dirs =
      if (flag) Seq.empty
      else if (!customDirs.isEmpty) customDirs.asScala
      else
        new File(projectDir, "/../scala") +: projectDirs.asScala.flatMap { x =>
          val file = getFile(x)
          if (file.toPath.startsWith(outputDir)) None else Some(file)
        }
    if (dirs.isEmpty) getLog.info(s"Skip scalafix[$flagName]")
    else dirs.foreach(dir => getLog.info(s"Processing[$flagName]: $dir"))
    dirs
  }
}
