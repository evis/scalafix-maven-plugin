# scalafix-maven-plugin

scalafix-maven-plugin enables automatic refactoring and linting of Maven projects written in Scala using [Scalafix](https://scalacenter.github.io/scalafix/).

## Installation

Add plugin into `plugins` node of `pom.xml`:

```xml
<plugins>
    <plugin>
        <groupId>io.github.evis</groupId>
        <artifactId>scalafix-maven-plugin</artifactId>
        <version>0.1.0_0.9.5</version>
    </plugin>
</plugins>
```

Where `0.1.0` is version of plugin itself, and `0.9.5` is version of Scalafix invoked by plugin.

Then, you need to setup a file `.scalafix.conf` in the root directory of your Maven project. You can find `.scalafix.conf` guide [here](https://scalacenter.github.io/scalafix/docs/users/configuration.html).

In order to execute semantic rules (e.g., `RemoveUnused`), you need to enable SemanticDB compiler plugin too:

```xml
<plugin>
    <groupId>net.alchim31.maven</groupId>
    <artifactId>scala-maven-plugin</artifactId>
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
                <artifactId>semanticdb-scalac_${scala.version}</artifactId>
                <version>${semanticdb.version}</version>
            </compilerPlugin>
        </compilerPlugins>
    </configuration>
</plugin>
```

You don't need to care about passing Scala version and Scalac options to this plugin specifically. Plugin finds them automatically from your build info.

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
        <artifactId>scalafix-maven-plugin</artifactId>
        <version>0.1.0_0.9.5</version>
        <configuration>
            <mode>CHECK</mode>
            <skipTest>true</skipTest>
        </configuration>
    </plugin>
</plugins>
```

### Plugin parameters

CLI name | Maven configuration name | Type | Description
--- | --- | --- | ---
`scalafix.mode` | `mode` | `ScalafixMainMode`: either `IN_PLACE`, `CHECK`, `STDOUT` or `AUTO_SUPPRESS_LINTER_ERRORS` (default: `IN_PLACE`) | Describes mode in which Scalafix runs. Description of different parameter values can be found in [Scalafix javadoc](https://static.javadoc.io/ch.epfl.scala/scalafix-interfaces/0.9.5/scalafix/interfaces/ScalafixMainMode.html).
`scalafix.command.line.args` | `commandLineArgs` | `String` (default: empty string) | Custom CLI arguments to pass into Scalafix. Description of available arguments can be found in [Scalafix CLI documentation](https://scalacenter.github.io/scalafix/docs/users/installation.html#help).
`scalafix.skip` | `skip` | `Boolean` (default: `false`) | Whether we should skip all formatting.
`scalafix.skip.main` | `skipMain` | `Boolean` (default: `false`) | Whether we should skip formatting of application/library sources (by default located in `main/scala`).
`scalafix.skip.test` | `skipTest` | `Boolean` (default: `false`) | Whether we should skip formatting of test sources (by default located in `/test/scala`).

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
                        <artifactId>scala-maven-plugin</artifactId>
                        <version>${scala-maven-plugin.version}</version>
                        <configuration>
                            <compilerPlugins>
                                <compilerPlugin>
                                    <groupId>org.scalameta</groupId>
                                    <artifactId>semanticdb-scalac_${scala.version}</artifactId>
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

## Contributing
Pull requests are welcome. For major changes, please [open an issue](https://github.com/evis/scalafix-maven-plugin/issues/new) first to discuss what you would like to change.
If you need some help with your PR at any time, please feel free to mention [`@evis`](https://github.com/evis).

## Support
The best way to get help is to [open an issue](https://github.com/evis/scalafix-maven-plugin/issues/new). You can do it for things like asking questions about the project or requesting technical help.

## License
[BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause)
