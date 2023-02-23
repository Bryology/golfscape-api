val CirceVersion = "0.14.4"
val GeotrellisVersion = "3.6.3"
val Http4sVersion = "0.23.18"
val LogbackVersion = "1.4.5"
val MunitCatsEffectVersion = "1.0.7"
val MunitVersion = "0.7.29"

lazy val root = (project in file("."))
  .settings(
    name := "golfscape-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.locationtech.geotrellis" %% "geotrellis-spark" % GeotrellisVersion excludeAll(
        ExclusionRule("org.typelevel", "cats-effect_2.13"),
        ExclusionRule("co.fs2", "fs2-core_2.13"),
        ExclusionRule("co.fs2", "fs2-io_2.13")
      ),
      "ch.qos.logback" % "logback-classic" % LogbackVersion % Runtime,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.scalameta" %% "munit" % MunitVersion % Test,
      "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test
    )
  )
