package io.github.evis.scalafix.maven.plugin.log

sealed abstract class LogLevel

object LogLevel {

  case object Debug extends LogLevel

  case object Info extends LogLevel

  case object Warn extends LogLevel

  case object Error extends LogLevel

  implicit val logLevelOrdering: Ordering[LogLevel] = Ordering.by {
    case Debug => 0
    case Info  => 1
    case Warn  => 2
    case Error => 3
  }
}
