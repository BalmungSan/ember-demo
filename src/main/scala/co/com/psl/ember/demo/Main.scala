package co.com.psl.ember.demo

import cats.effect.{ExitCode, ContextShift, IO, IOApp, Resource, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.{EntityEncoder, HttpApp, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.{Router, Server}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    HttpServer.program.compile.drain.as(ExitCode.Success)
}

object HttpServer {
  private val app: HttpApp[IO] =
    Router(
      "/" -> Service.getAgeByName
    ).orNotFound

  /**
  private def server(logger: Logger[IO])
                    (implicit cs: ContextShift[IO], timer: Timer[IO]): Resource[IO, Server[IO]] =
    BlazeServerBuilder[IO]
      .bindHttp(host = "0.0.0.0", port = 8080)
      .withHttpApp(app)
      .resource
  */

  private def server(logger: Logger[IO])
                    (implicit cs: ContextShift[IO], timer: Timer[IO]): Resource[IO, Server[IO]] =
    EmberServerBuilder
      .default[IO]
      .withHost("0.0.0.0")
      .withPort(8080)
      .withHttpApp(app)
      .withLogger(logger)
      .build

  def program(implicit cs: ContextShift[IO], timer: Timer[IO]): Stream[IO, Unit] =
    for {
      logger <- Stream.eval(Slf4jLogger.create[IO])
      server <- Stream.resource(server(logger))
      _ <- Stream.eval(logger.info(s"Started server on: ${server.address}"))
      _ <- Stream.never[IO].covaryOutput[Unit]
    } yield ()
}

object Service extends Http4sDsl[IO] {
  private val db: Map[String, Int] = Map(
    "balmungsan" -> 23
  )

  private implicit val ageEncoder: EntityEncoder[IO, Int] = EntityEncoder.showEncoder

  val getAgeByName: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "age" / name =>
      Ok(
        IO.fromEither(
          db
            .get(key = name.toLowerCase)
            .toRight(left = new IllegalArgumentException(s"${name} not found in the database"))
        )
      )
  }
}
