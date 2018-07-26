package com.scalents.embeddedserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

trait EmbeddedHttpServer {

  def withEmbeddedServer[T](host: String = "localhost", inPort: Int = 8080)(routes: Route)(block: => T)(implicit system: ActorSystem, mat: ActorMaterializer, ec: ExecutionContext): T  = {
    val server = Http().bindAndHandle(routes, host, inPort)
    try {
      block
    } finally {
      server.flatMap { _.unbind() }
    }
  }
}

object EmbeddedHttpServer extends EmbeddedHttpServer
