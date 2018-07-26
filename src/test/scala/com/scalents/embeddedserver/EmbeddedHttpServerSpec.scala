package com.scalents.embeddedserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

class EmbeddedHttpServerSpec extends FlatSpec with Matchers with EmbeddedHttpServer with ScalaFutures {

  implicit val aSys: ActorSystem = ActorSystem("test")
  implicit val materializer = ActorMaterializer()

  implicit val defaultPatience: PatienceConfig = PatienceConfig(timeout = Span(120000, Seconds), interval = Span(15, Millis))


  "The embedded server" should "respond with hello" in {
    val routes = path("hi") { get { complete("Hello")} }
    withEmbeddedServer(inPort = 8989)(routes){

      val result = Http().singleRequest(HttpRequest(uri = Uri("http://localhost:8989/hi"))).futureValue
      val HttpResponse(StatusCodes.OK, _, entity, _) = result
      val body = entity.dataBytes.runWith(Sink.seq).futureValue
      body.head.utf8String shouldBe "Hello"

    }
  }
}