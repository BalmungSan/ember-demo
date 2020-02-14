// Project settings.
name := "ember-demo"
organization := "co.com.psl"
organizationName := "PSL S.A.S."
organizationHomepage := Some(url("http://www.psl.com.co/"))
version := "0.1.0"
scalaVersion := "2.13.1"

// Better monadic for.
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")

// Application dependencies.
val CatsEffectVersion     = "2.1.1"
val CatsVersion           = "2.1.0"
val Fs2Version            = "2.2.2"
val Http4sVersion         = "0.21.0"
val Log4CatsVersion       = "1.0.1"
val LogbackClassicVersion = "1.2.3"

libraryDependencies ++= Seq(
  "ch.qos.logback"     % "logback-classic"     % LogbackClassicVersion,
  "co.fs2"            %% "fs2-core"            % Fs2Version,
  "co.fs2"            %% "fs2-io"              % Fs2Version,
  "io.chrisdavenport" %% "log4cats-slf4j"      % Log4CatsVersion,
  "org.http4s"        %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"        %% "http4s-core"         % Http4sVersion,
  "org.http4s"        %% "http4s-dsl"          % Http4sVersion,
  "org.http4s"        %% "http4s-ember-server" % Http4sVersion,
  "org.typelevel"     %% "cats-core"           % CatsVersion,
  "org.typelevel"     %% "cats-effect"         % CatsEffectVersion
)
