package com.kwk.golfscape.model

import geotrellis.raster.{ColorMap, Histogram}
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.render.{ColorRamps, LessThan}
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import java.nio.file.Path
import java.util.UUID

case class Feature(
                    id: UUID,
                    courseName: String,
                    hole: String,
                    featureType: String,
                    center: Seq[Double],
                    extent: Seq[Double],
                    img: Array[Byte]
                  )

object Feature {
  implicit val featureEncoder: Encoder[Feature] = deriveEncoder[Feature]
  implicit val featureListEncoder: Encoder[List[Feature]] = deriveEncoder[List[Feature]]

  implicit def featureEntityEncoder[F[_]]: EntityEncoder[F, Feature] = jsonEncoderOf

  implicit def featureListEntityEncoder[F[_]]: EntityEncoder[F, List[Feature]] = jsonEncoderOf

  def apply(path: Path): Feature = {
    val courseName = path.toString.split("/")(1)
    val tif = GeoTiffReader.readSingleband(path.toString)
    val extent = tif.extent

    Feature(
      UUID.randomUUID(),
      courseName,
      path.getFileName.toString.split("\\.").head,
      "green",
      Seq(extent.center.getX, extent.center.getY),
      Seq(extent.xmin, extent.ymin, extent.xmax, extent.ymax),
      tif.tile.renderPng(generateColorMap(tif.tile.histogramDouble())).bytes
    )
  }

  private def generateColorMap(histogram: Histogram[Double]): ColorMap =
    ColorRamps.BlueToRed
      .stops(histogram.bucketCount())
      .toColorMap(histogram, ColorMap.Options(LessThan, 0x00000000, 0x00000000, strict = false))
}
