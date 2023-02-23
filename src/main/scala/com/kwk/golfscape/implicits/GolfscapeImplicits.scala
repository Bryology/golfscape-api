package com.kwk.golfscape.implicits

import java.nio.file.{FileSystems, Files, Path}
import scala.jdk.CollectionConverters._

object GolfscapeImplicits {
  implicit class ImplicitPath(path: String) {
    def subdirectories: LazyList[Path] =
      Files
        .list(FileSystems.getDefault.getPath(path))
        .iterator
        .asScala
        .to(LazyList)
  }
}
