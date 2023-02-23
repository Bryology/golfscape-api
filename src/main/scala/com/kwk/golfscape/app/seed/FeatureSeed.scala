package com.kwk.golfscape.app.seed

import com.kwk.golfscape.implicits.GolfscapeImplicits._
import com.kwk.golfscape.model.Feature

import java.nio.file.Path

object FeatureSeed {
  val features: LazyList[Feature] =
    "course-data"
      .subdirectories
      .flatMap(toFeatures)

  private def toFeatures(path: Path): LazyList[Feature] =
    s"$path/tif/green"
      .subdirectories
      .map(Feature.apply)

}
