package com.kwk.golfscape.app

import cats.effect._
import cats.syntax.all._
import com.comcast.ip4s._
import com.kwk.golfscape.contoller.FeatureController
import com.kwk.golfscape.repository.FeatureRepository
import com.kwk.golfscape.service.FeatureService
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object GolfscapeServer extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    GolfscapeServer.stream[IO].compile.drain.as(ExitCode.Success)

  def stream[F[_] : Async]: Stream[F, Nothing] = {
    //repositories
    implicit val featureRepository: FeatureRepository[F] = FeatureRepository.impl[F]

    //services
    implicit val featureService: FeatureService[F] = FeatureService.impl[F]

    for {
      _ <- Stream.resource(EmberClientBuilder.default[F].build)
      featureAlg = FeatureController.impl[F]

      httpApp = GolfscapeRoutes.featureRoutes[F](featureAlg)
        .orNotFound

      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

      exitCode <- Stream.resource(
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .withErrorHandler {
            case e => e.printStackTrace().asInstanceOf[F[_]].as(org.http4s.Response.timeout[F])
          }
          .build >> Resource.eval(Async[F].never)
      )
    } yield exitCode
  }.drain
}
