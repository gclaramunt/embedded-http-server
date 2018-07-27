package com.scalents.embeddedserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext

trait EmbeddedHttpServer {

  val shutdownDeadline: FiniteDuration = 10.seconds

  def withEmbeddedServer[T](host: String = "localhost", port: Int = 8080, routes: Route)(block: => T)
                           (implicit system: ActorSystem, mat: ActorMaterializer, ec: ExecutionContext): T  = {
    val server = Http().bindAndHandle(routes, host, port)
    try {
      block
    } finally {
      server.flatMap(_.terminate(shutdownDeadline))
    }
  }
}

object EmbeddedHttpServer extends EmbeddedHttpServer
