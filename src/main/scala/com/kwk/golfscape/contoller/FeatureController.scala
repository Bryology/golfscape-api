package com.kwk.golfscape.contoller

import cats.effect.Concurrent
import com.kwk.golfscape.model.Feature
import com.kwk.golfscape.service.FeatureService

trait FeatureController[F[_]] {
  def get(id: String): F[Feature]

  def all: F[List[Feature]]
}

object FeatureController {
  def apply[F[_]](implicit ev: FeatureController[F]): FeatureController[F] = ev

  def impl[F[_] : Concurrent](implicit service: FeatureService[F]): FeatureController[F] =
    new FeatureController[F] {
      override def get(id: String): F[Feature] = service.get(id)

      override def all: F[List[Feature]] = service.all
    }
}
