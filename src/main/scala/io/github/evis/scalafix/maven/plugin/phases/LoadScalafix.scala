package io.github.evis.scalafix.maven.plugin.phases

import scalafix.interfaces.Scalafix

trait LoadScalafix {

  def loadScalafix(): Scalafix = {
    val pluginClassLoaderWithScalafix = getClass.getClassLoader
    Scalafix.classloadInstance(pluginClassLoaderWithScalafix)
  }
}
