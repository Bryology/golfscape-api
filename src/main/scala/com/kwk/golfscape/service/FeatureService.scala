package com.kwk.golfscape.service

import cats.effect.Concurrent
import com.kwk.golfscape.model.Feature
import com.kwk.golfscape.repository.FeatureRepository

import java.util.UUID


trait FeatureService[F[_]] {
  def get(id: String): F[Feature]

  def all: F[List[Feature]]
}

object FeatureService {
  def impl[F[_] : Concurrent](implicit repository: FeatureRepository[F]): FeatureService[F] =
    new FeatureService[F] {
      override def get(id: String): F[Feature] =
        repository.get(UUID.fromString(id))


      override def all: F[List[Feature]] =
        repository
          .all
          .compile
          .toList
    }
}
