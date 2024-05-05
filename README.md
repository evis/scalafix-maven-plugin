# scalafix-maven-plugin

scalafix-maven-plugin enables automatic refactoring and linting of Maven projects written in Scala using [Scalafix](https://scalacenter.github.io/scalafix/).

## Installation

Add plugin into `plugins` node of `pom.xml`:

```xml
<plugins>
    <plugin>
        <groupId>io.github.evis</groupId>
        <artifactId>scalafix-maven-plugin_2.13</artifactId>
        <version>0.1.9_0.12.1</version>
    </plugin>
</plugins>
```

Where `0.1.9` is version of the plugin itself, and `0.12.1` is version of Scalafix invoked by the plugin.

Then, you need to setup a file `.scalafix.conf` in the root directory of your Maven project (note the dot at the start of filename). You can find `.scalafix.conf` guide [here](https://scalacenter.github.io/scalafix/docs/users/configuration.html).

You don't need to care about passing Scala version and Scalac options to this plugin specifically. Plugin finds them automatically from your build info.

### Semantic rules with SemanticDB

In order to execute semantic rules (e.g., `RemoveUnused`), you need to enable SemanticDB.

<details>
<summary>For Scala 2, you should add compiler plugin.</summary>

```xml
<plugin>
    <groupId>net.alchim31.maven</groupId>
    <artifactId>scala-maven-plugin_${scala.binary.version}</artifactId>
    <version>${scala-maven-plugin.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>testCompile</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <args>
            <arg>-Ywarn-unused</arg> <!-- if you need exactly RemoveUnused -->
        </args>
        <compilerPlugins>
            <compilerPlugin>
                <groupId>org.scalameta</groupId>
                <artifactId>semanticdb-scalac_${scala.binary.version}</artifactId>
                <version>${semanticdb.version}</version>
            </compilerPlugin>
        </compilerPlugins>
    </configuration>
</plugin>
```
</details>

<details>
<summary>For Scala 3, you just need to enable it with compiler flag.</summary>

```xml
<plugin>
    <groupId>net.alchim31.maven</groupId>
    <artifactId>scala-maven-plugin_${scala.binary.version}</artifactId>
    <version>${scala-maven-plugin.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>testCompile</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <args>
            <arg>-Ywarn-unused</arg> <!-- if you need exactly RemoveUnused -->
            <arg>-Ysemanticdb</arg>
        </args>
    </configuration>
</plugin>
```
</details>

### Overriding sources location

By default, sources should be located inside `src/main/scala` directory. Though, you may change it using `sourceDirectory` build option, plugin respects this, e.g.:

```xml
<build>
    <sourceDirectory>src/main/my-sources-dir</sourceDirectory>
    <!-- another build settings... -->
</build>
```

Or using `mainSourceDirectories` configuration option of the plugin, e.g.:

```xml
<plugin>
    <groupId>io.github.evis</groupId>
    <artifactId>scalafix-maven-plugin_2.13</artifactId>
    <version>0.1.9_0.12.1</version>
    <configuration>
        <mainSourceDirectories>
            <directory>src/main/my-sources-dir</directory>
            <directory>src/main/my-another-dir</directory>
            <!-- and so on, you can list several directories here... -->
        </mainSourceDirectories>
    </configuration>
</plugin>
```

## Usage

There is one Maven goal in this plugin called `scalafix:scalafix`. It executes Scalafix with given config on your sources.

First of all, you can invoke it via CLI:

```bash
mvn scalafix:scalafix
```

If there are some errors while running Scalafix, then the build will fail. Otherwise, it will succeed.

In order to execute semantic rules (e.g., `RemoveUnused`), you also need to compile sources beforehand, e.g.:

```bash
mvn compile test-compile scalafix:scalafix
```

You can tweak plugin execution using some parameters, e.g.:

```bash
mvn scalafix:scalafix -Dscalafix.mode=CHECK -Dscalafix.skipTest=true
```

Also, you can pass parameters via `pom.xml`:

```xml
<plugins>
    <plugin>
        <groupId>io.github.evis</groupId>
        <artifactId>scalafix-maven-plugin_2.13</artifactId>
        <version>0.1.9_0.12.1</version>
        <configuration>
            <mode>CHECK</mode>
            <skipTest>true</skipTest>
        </configuration>
    </plugin>
</plugins>
```

If you want to use external rules, add jars containing rules to dependencies of the plugin:

```xml
<plugin>
    <groupId>io.github.evis</groupId>
    <artifactId>scalafix-maven-plugin_2.13</artifactId>
    <version>0.1.9_0.12.1</version>
    <dependencies>
        <dependency>
            <groupId>com.nequissimus</groupId>
            <artifactId>sort-imports_2.13</artifactId>
            <version>0.6.1</version>
        </dependency>
    </dependencies>
</plugin>
```

### Plugin parameters

CLI name | Maven configuration name | Maven type | Description
--- | --- | --- | ---
`scalafix.mode` | `mode` | `ScalafixMainMode`: either `IN_PLACE`, `CHECK`, `STDOUT` or `AUTO_SUPPRESS_LINTER_ERRORS` (default: `IN_PLACE`) | Describes mode in which Scalafix runs. Description of different parameter values can be found in [Scalafix javadoc](https://static.javadoc.io/ch.epfl.scala/scalafix-interfaces/0.11.0/scalafix/interfaces/ScalafixMainMode.html).
`scalafix.command.line.args` | `commandLineArgs` | `List[String]` (default: empty) | Custom CLI arguments to pass into Scalafix. Description of available arguments can be found in [Scalafix CLI documentation](https://scalacenter.github.io/scalafix/docs/users/installation.html#help).
`scalafix.skip` | `skip` | `Boolean` (default: `false`) | Whether we should skip all formatting.
`scalafix.skip.main` | `skipMain` | `Boolean` (default: `false`) | Whether we should skip formatting of application/library sources (by default located in `main/scala`).
`scalafix.skip.test` | `skipTest` | `Boolean` (default: `false`) | Whether we should skip formatting of test sources (by default located in `/test/scala`).
`scalafix.config` | `config` | `File` (default: `.scalafix.conf`) | Configuration with rules to invoke for Scalafix.
`scalafix.mainSourceDirectories` | `mainSourceDirectories` | `List[File]` (default: see below) | Which main source directories to format.
`scalafix.testSourceDirectories` | `testSourceDirectories` | `List[File]` (default: see below) | Which test source directories to format.

> The plugin determines code paths to process the same way the compiler would; by default, from
> `build.sourceDirectory` property, but could be added by another plugin 
> (e.g., `build-helper-maven-plugin` and `scala-maven-plugin` have a way to define multiple paths).
>
> If a plugin defines those paths, be sure to invoke `mvn` with the phase in which that happens;
> for instance: `mvn initialize scalafix:scalafix`.

### Tips and tricks

1. `scalafix.mode=AUTO_SUPPRESS_LINTER_ERRORS` is useful on the first execution of Scalafix: it allows to effectively ignore warnings in existing large codebase.
2. `scalafix.mode=CHECK` is convenient to use in continuous integration builds: e.g., to disallow merging code with identified problems.
3. `scalafix.skip=true` is especially useful for Maven modules which don't contain Scala sources at all.
4. `scalafix.command.line` is great for applying those Scalafix parameters which aren't directly supported by plugin yet.
5. SemanticDB compiler plugin may slow down compilation a little bit. If you want to use it only for Scalafix, you can make separate Maven profile, e.g.:

```xml
<profiles>
    <profile>
        <id>semanticdb</id>
        <build>
            <pluginManagement>
                <plugins>
                    <plugin>
                        <groupId>net.alchim31.maven</groupId>
                        <artifactId>scala-maven-plugin_${scala.binary.version}</artifactId>
                        <version>${scala-maven-plugin.version}</version>
                        <configuration>
                            <compilerPlugins>
                                <compilerPlugin>
                                    <groupId>org.scalameta</groupId>
                                    <artifactId>semanticdb-scalac_${scala.binary.version}</artifactId>
                                    <version>${semanticdb.version}</version>
                                </compilerPlugin>
                            </compilerPlugins>
                        </configuration>
                    </plugin>
                </plugins>
            </pluginManagement>
        </build>
    </profile>
</profiles>
```

Then, run Scalafix like this:

```bash
mvn clean compile test-compile scalafix:scalafix -Psemanticdb
```

So, when you run compilation with profile turned off, then SemanticDB compiler plugin doesn't affect compilation time at all.

6. Sometimes it makes sense to make different requirements about main and test code quality. To achieve it you can make separate test config, and invoke plugin twice, e.g.:

```bash
mvn clean compile scalafix:scalafix # this invocation uses .scalafix.conf
mvn test-compile scalafix:scalafix -Dscalafix.config=.scalafix.test.conf # means that rules for test code are located in file .scalafix.test.conf
```

7. If you need to run a rule from Github, add it to the list of rules inside `.scalafix.conf`:
```hocon
rules = [
  "github:zio/zio/Zio2Upgrade?sha=series/2.x"
]
```

And then just run `mvn scalafix:scalafix`.

This is equivalent to `sbt "scalafixEnable; scalafixAll github:zio/zio/Zio2Upgrade?sha=series/2.x"`.

8. To use a third-party rule, you may need to add its dependencies if the compiler complains about missing classes or methods. For example, scala-rewrites requires scalafix-core and typelevel-scalafix:

```xml
<plugin>
    <groupId>io.github.evis</groupId>
    <artifactId>scalafix-maven-plugin_${scala.version.short}</artifactId>
    <version>0.1.9_${scalafix.version}</version>
    <dependencies>
        <dependency>
            <groupId>ch.epfl.scala</groupId>
            <artifactId>scalafix-core_${scala.version.short}</artifactId>
            <version>${scalafix.version}</version>
        </dependency>
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-rewrites_${scala.version.short}</artifactId>
            <version>0.1.3</version>
        </dependency>
        <dependency>
            <groupId>org.typelevel</groupId>
            <artifactId>typelevel-scalafix_${scala.version.short}</artifactId>
            <version>0.1.4</version>
        </dependency>
    </dependencies>
</plugin>
```

## Contributing
Pull requests are welcome. For major changes, please [open an issue](https://github.com/evis/scalafix-maven-plugin/issues/new) first to discuss what you would like to change.
If you need some help with your PR at any time, please feel free to mention [`@evis`](https://github.com/evis).

## Support
The best way to get help is to [open an issue](https://github.com/evis/scalafix-maven-plugin/issues/new). You can do it for things like asking questions about the project or requesting technical help.

## License
[BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause)
