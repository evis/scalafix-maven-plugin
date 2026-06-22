package io.github.evis.scalafix.maven.plugin.params

import org.apache.maven.model.Plugin
import org.codehaus.plexus.util.xml.Xpp3Dom

import scala.util.Try

object PluginsParam {

  val scalaPluginOrgs = Set(
    "net.alchim31.maven",
    "com.triplequote.maven"
  )

  def apply(plugins: Iterable[Plugin]): MojoParam = {
    _.withScalacOptions(
      plugins.findScalaPlugin.flatMap(config).toList.flatMap(compilerArgs)
    )
  }

  implicit private class PluginsOps(private val plugins: Iterable[Plugin])
      extends AnyVal {

    def findScalaPlugin: Option[Plugin] = {
      plugins.find { plugin =>
        scalaPluginOrgs.contains(
          plugin.getGroupId
        ) && plugin.getArtifactId == "scala-maven-plugin"
      }
    }
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.asInstanceOf"))
  private def config(plugin: Plugin): Option[Xpp3Dom] = {
    Try(Option(plugin.getConfiguration.asInstanceOf[Xpp3Dom])).getOrElse(None)
  }

  private def compilerArgs(config: Xpp3Dom): List[String] = {
    config
      .getChildren("args")
      .toList
      .flatMap(_.getChildren("arg").toList)
      .map(_.getValue)
  }
}
