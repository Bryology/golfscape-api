package com.kwk.golfscape.app

import cats.effect.Sync
import cats.implicits._
import com.kwk.golfscape.contoller.FeatureController
import org.http4s.{Header, HttpRoutes, MediaRange}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Accept
import org.typelevel.ci.CIStringSyntax

object GolfscapeRoutes {
  def featureRoutes[F[_] : Sync](controller: FeatureController[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "feature" / id =>
        for {
          feature <- controller.get(id)
          resp <- Ok(feature)
        } yield resp.withHeaders(Header.Raw(ci"Access-Control-Allow-Origin", "*"))
      case GET -> Root / "features" =>
        for {
          features <- controller.all
          resp <- Ok(features)
        } yield resp.withHeaders(Header.Raw(ci"Access-Control-Allow-Origin", "*"))
    }
  }
}
