package com.kwk.golfscape

import cats.effect.{ExitCode, IO, IOApp}
import com.kwk.golfscape.app.GolfscapeServer

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    GolfscapeServer.stream[IO].compile.drain.as(ExitCode.Success)
}
