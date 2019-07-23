package io.github.evis.scalafix.maven.plugin.log

import io.github.evis.scalafix.maven.plugin.log.LogLevel._
import org.apache.maven.plugin.logging.Log

import scala.Ordered._
import scala.collection.immutable.{Queue, Seq}

class TestLog(level: LogLevel) extends Log {

  def getLog: Seq[LogMessage] = log

  override def isDebugEnabled: Boolean = isEnabled(Debug)

  override def isInfoEnabled: Boolean = isEnabled(Info)

  override def isWarnEnabled: Boolean = isEnabled(Warn)

  override def isErrorEnabled: Boolean = isEnabled(Error)

  override def debug(s: CharSequence): Unit = add(Debug, s)

  override def debug(s: CharSequence, e: Throwable): Unit = add(Debug, s, e)

  override def debug(e: Throwable): Unit = add(Debug, e)

  override def info(s: CharSequence): Unit = add(Info, s)

  override def info(s: CharSequence, e: Throwable): Unit = add(Info, s, e)

  override def info(e: Throwable): Unit = add(Info, e)

  override def warn(s: CharSequence): Unit = add(Warn, s)

  override def warn(s: CharSequence, e: Throwable): Unit = add(Warn, s, e)

  override def warn(e: Throwable): Unit = add(Warn, e)

  override def error(s: CharSequence): Unit = add(Error, s)

  override def error(s: CharSequence, e: Throwable): Unit = add(Error, s, e)

  override def error(e: Throwable): Unit = add(Error, e)

  private var log = Queue.empty[LogMessage]

  private def add(level: LogLevel, s: CharSequence): Unit =
    add(level, Some(s), e = None)

  private def add(level: LogLevel, s: CharSequence, e: Throwable): Unit =
    add(level, Some(s), Some(e))

  private def add(level: LogLevel, e: Throwable): Unit =
    add(level, s = None, Some(e))

  private def add(
      level: LogLevel,
      s: Option[CharSequence],
      e: Option[Throwable]): Unit =
    log = log.enqueue(LogMessage(level, s, e))

  private def isEnabled(level: LogLevel) = level >= this.level
}
