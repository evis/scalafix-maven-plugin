package io.github.evis.scalafix.maven.plugin.params

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

import scala.language.implicitConversions

import org.apache.maven.model.Build
import org.apache.maven.model.Model
import org.apache.maven.project.MavenProject

class SourceDirectoryLookupSpec extends ParamSpec {

  import SourceDirectoryLookupSpec._

  it should "add nonempty main" in {
    // doesn't check for existence
    val builder = new SourceDirectoryLookup(TestFileOpsNone, getMavenProject())
    builder.getMain(Seq("foo", "/bar", "foo")) should contain theSameElementsAs
      List[Path]("/xyz/foo", "/bar")
  }

  it should "add empty main" in {
    val mavenBuild = new Build() {
      setSourceDirectory("/foo/src/main/java")
    }

    val builder =
      new SourceDirectoryLookup(TestFileOpsAll, getMavenProject(mavenBuild))
    builder.getMain(Nil) should contain theSameElementsAs
      List[Path]("/foo/src/main/scala")
  }

  it should "add empty main, checks for existence" in {
    val mavenBuild = new Build() {
      setSourceDirectory("/foo/src/main/java")
    }

    val builder =
      new SourceDirectoryLookup(TestFileOpsNone, getMavenProject(mavenBuild))
    builder.getMain(Nil) shouldBe empty
  }

  it should "add nonempty test" in {
    val builder = new SourceDirectoryLookup(TestFileOpsAll, getMavenProject())
    builder.getTest(Seq("foo", "/bar", "/bar")) should contain theSameElementsAs
      List[Path]("/xyz/foo", "/bar")
  }

  it should "add empty test" in {
    val mavenBuild = new Build() {
      setTestSourceDirectory("/foo/src/test/scala")
    }

    val builder =
      new SourceDirectoryLookup(TestFileOpsAll, getMavenProject(mavenBuild))
    builder.getTest(Nil) should contain theSameElementsAs
      List[Path]("/foo/src/test/scala")
  }

  it should "add empty test, checks for existence" in {
    val mavenBuild = new Build() {
      setTestSourceDirectory("/foo/src/test/java")
    }

    val builder =
      new SourceDirectoryLookup(TestFileOpsNone, getMavenProject(mavenBuild))
    builder.getTest(Nil) shouldBe empty
  }

  it should "add empty main, filtering build dir" in {
    val mavenBuild = new Build() {
      setSourceDirectory("/foo/src/main/java")
    }
    val project = getMavenProject(mavenBuild)
    project.addCompileSourceRoot("/foo/src/main/scala")
    project.addCompileSourceRoot("/out/src/main/scala")
    project.addCompileSourceRoot("/out2/src/main/scala")

    val builder = new SourceDirectoryLookup(TestFileOpsAll, project)
    builder.getMain(Nil) should contain theSameElementsAs
      List[Path]("/foo/src/main/scala", "/out2/src/main/scala")
  }

  it should "add empty test, filtering build dir" in {
    val mavenBuild = new Build() {
      setTestSourceDirectory("/foo/src/test/java")
    }
    val project = getMavenProject(mavenBuild)
    project.addTestCompileSourceRoot("/foo/src/test/scala")
    project.addTestCompileSourceRoot("/out/src/test/scala")
    project.addTestCompileSourceRoot("/out2/src/test/scala")

    val builder = new SourceDirectoryLookup(TestFileOpsAll, project)
    builder.getTest(Nil) should contain theSameElementsAs
      List[Path]("/foo/src/test/scala", "/out2/src/test/scala")
  }

}

private object SourceDirectoryLookupSpec {

  def getMavenProject(mavenBuild: Build): MavenProject = {
    mavenBuild.setDirectory("/out")
    new MavenProject(new Model { setBuild(mavenBuild) }) {
      setFile(new File("/xyz/pom.xml")) // sets basedir
    }
  }

  def getMavenProject(): MavenProject = getMavenProject(new Build())

  implicit def implicitStringToPath(file: String): Path = Paths.get(file)

  implicit def implicitStringToFile(file: String): File =
    implicitStringToPath(file).toFile

  object TestFileOpsAll extends FileOps {
    override def exists(file: File): Boolean = true
    override def getPath(path: String): Path = Paths.get(path)
  }

  object TestFileOpsNone extends FileOps {
    override def exists(file: File): Boolean = false
    override def getPath(path: String): Path = Paths.get(path)
  }

}
