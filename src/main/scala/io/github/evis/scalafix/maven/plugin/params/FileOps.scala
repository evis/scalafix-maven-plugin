package io.github.evis.scalafix.maven.plugin.params

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

abstract class FileOps {

  def exists(file: File): Boolean
  def getPath(path: String): Path

  def getFile(path: String) = getPath(path).toFile

}

object FileOps extends FileOps {

  override def exists(file: File): Boolean = file.exists()

  override def getPath(path: String) = Paths.get(path)

}
