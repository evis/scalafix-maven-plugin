package io.github.evis.scalafix.maven.plugin.params

class CompiledDirectoryParamSpec extends ParamSpec {

  "CompiledDirectoryParam" should "add existing classpath" in {
    CompiledDirectoryParam("src/main/scala").applied.classpath.loneElement.toString shouldBe "src/main/scala"
  }

  it should "not add non-existing classpath" in {
    CompiledDirectoryParam("src/main/unexisting").applied.classpath shouldBe empty
  }

  it should "add all classpaths if invoked with multiple" in {
    val result =
      CompiledDirectoryParam("src/main/scala", "src/test/scala").applied.classpath
        .map(_.toString)
    result should contain theSameElementsAs List(
      "src/main/scala",
      "src/test/scala")
  }
}
