package com.kwk.golfscape.repository

import cats.Monad
import cats.data.OptionT
import cats.effect.Concurrent
import com.kwk.golfscape.app.seed.FeatureSeed
import com.kwk.golfscape.model.Feature

import java.util.UUID


trait FeatureRepository[F[_]] {
  def all: fs2.Stream[F, Feature]

  def get(id: UUID): F[Feature]
}

object FeatureRepository {
  def impl[F[_] : Concurrent]: FeatureRepository[F] =
    new FeatureRepository[F] {
      private lazy val features: Seq[Feature] = FeatureSeed.features

      override def all: fs2.Stream[F, Feature] = fs2.Stream(features: _*)

      override def get(id: UUID): F[Feature] = Monad[F].pure(features.head)
    }
}
