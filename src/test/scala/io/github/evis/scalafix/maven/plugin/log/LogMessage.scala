package io.github.evis.scalafix.maven.plugin.log

final case class LogMessage(
    level: LogLevel,
    charSequence: Option[CharSequence],
    throwable: Option[Throwable]
)
