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

  @Parameter(
    property = "project",
    defaultValue = "${project}",
    required = true,
    readonly = true
  )
  private var project: MavenProject = _

  @Parameter(property = "scalafix.mainSourceDirectories")
  private var mainSourceDirectories: JList[File] = _

  @Parameter(property = "scalafix.testSourceDirectories")
  private var testSourceDirectories: JList[File] = _

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
        "Skip scalafix since both skip.main and skip.test flags passed"
      )
    } else {
      val bld = project.getBuild
      val mainOutputs = if (skipMain) Nil else List(bld.getOutputDirectory)
      val testOutputs = if (skipTest) Nil else List(bld.getTestOutputDirectory)
      val params =
        List(
          getSourceParam,
          ProjectDependenciesParam(project.getArtifacts.asScala),
          CompiledDirectoryParam(mainOutputs ++ testOutputs),
          PluginsParam(bld.getPlugins.asScala),
          ModeParam(mode),
          ConfigParam(config),
          CommandLineArgsParam(commandLineArgs)
        )
      run(params, getLog)
    }
  }

  private def getSourceParam: MojoParam = {
    val lookup = new SourceDirectoryLookup(FileOps, project)
    val main = checkSources("main", skipMain)(
      lookup.getMain(mainSourceDirectories.asScala)
    )
    val test = checkSources("test", skipTest)(
      lookup.getTest(testSourceDirectories.asScala)
    )
    SourceDirectoryParam(main ++ test)
  }

  private def checkSources(flagName: String, skip: Boolean)(
      getPaths: => List[Path]
  ): List[Path] = {
    val paths = if (skip) Nil else getPaths
    if (paths.isEmpty) getLog.info(s"Skip scalafix[$flagName]")
    else paths.foreach(dir => getLog.info(s"Processing[$flagName]: $dir"))
    paths
  }

}
