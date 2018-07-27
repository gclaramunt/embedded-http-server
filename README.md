# embedded-http-server
Simple embedded akka http server for component testing

A pretty minimal way to embed a web server for your component tests

## Usage

Import the object

    import com.scalents.embeddedserver.EmbeddedHttpServer._

Then, surround the code that does the call with

    withEmbeddedServer(routes){
        block that does a http call
    }


That's it :)

## Notes
 * By default the server is named "localhost" and binds to the 8080 port, but the values can be overridden
 * The routes are defined as akka http routes.

## A simple example

( See [EmbeddedHttpServerSpec.scala](./src/test/scala/com/scalents/embeddedserver/EmbeddedHttpServerSpec.scala) )

Let's create an embedded server that responds with "Hello" when we do a http GET on port 8989 and validate the response

    "The embedded server" should "respond with hello" in {
        withEmbeddedServer(port = 8989, routes = path("hi") { get { complete("Hello")} }){

          val result = Http().singleRequest(HttpRequest(uri = Uri("http://local:8989/hi"))).futureValue
          val HttpResponse(StatusCodes.OK, _, entity, _) = result
          val body = entity.dataBytes.runWith(Sink.seq).futureValue
          body.head.utf8String shouldBe "Hello"

        }
      }
