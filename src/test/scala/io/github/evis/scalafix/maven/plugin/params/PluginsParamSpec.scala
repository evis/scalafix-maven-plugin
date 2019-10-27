package io.github.evis.scalafix.maven.plugin.params

import java.io.StringReader

import io.github.evis.scalafix.maven.plugin.{BaseSpec, ScalafixArgumentsBuilder}
import org.apache.maven.model.Plugin
import org.codehaus.plexus.util.xml.Xpp3DomBuilder

import scala.collection.JavaConverters._

class PluginsParamSpec extends BaseSpec {

  def scalaPlugin(args: List[String]): Plugin = {
    val sp = new Plugin()
    sp.setGroupId("net.alchim31.maven")
    sp.setArtifactId("scala-maven-plugin")
    if (args.nonEmpty) {
      val argsLine = args.mkString("<arg>", "</arg><arg>", "</arg>")
      val configurationString = s"""<configuration><args>$argsLine</args></configuration>"""
      sp.setConfiguration(
        Xpp3DomBuilder.build(new StringReader(configurationString))
      )
    }
    sp
  }

  "PluginsParam" should "return scalacOptions if they exist" in {
    PluginsParam(List(scalaPlugin("-Xlint" :: "-Ypartial-unification" :: Nil)).asJava)(ScalafixArgumentsBuilder())
      .scalacOptions shouldBe List("-Xlint", "-Ypartial-unification")
  }

  it should "return no scalacOptions if they are not set" in {
    PluginsParam(List(scalaPlugin(Nil)).asJava)(ScalafixArgumentsBuilder())
      .scalacOptions shouldBe Nil
  }

}
