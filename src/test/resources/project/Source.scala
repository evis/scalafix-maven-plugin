object Source {

  implicit class LeakingOps(val leaking: Int) extends AnyVal {
    def f: Int = leaking * 2
  }
}
