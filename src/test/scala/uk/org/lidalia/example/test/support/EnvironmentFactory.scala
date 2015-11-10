package uk.org.lidalia.example.test.support

import uk.org.lidalia.example.server.application.ApplicationConfig
import uk.org.lidalia.example.server.web.{ServerFactory, ServerConfig, WebConfig}
import uk.org.lidalia.lang.ResourceFactory
import uk.org.lidalia.lang.ResourceFactory.withAll
import uk.org.lidalia.stubhttp.StubHttpServerFactory

class EnvironmentFactory extends ResourceFactory[Environment] {
  val stub1Factory = StubHttpServerFactory()
  val stub2Factory = StubHttpServerFactory()
  override def withA[T](work: (Environment) => T): T = {
    withAll(stub1Factory, stub2Factory) { (stub1, stub2) =>
      val config = ServerConfig(
        ApplicationConfig(
          sendGridUrl = stub1.localAddress,
          contentfulUrl = stub2.localAddress
        ),
        WebConfig(
          localPort = None
        )
      )
      new ServerFactory(config).withA { application =>
        work(new Environment(stub1, stub2, application))
      }
    }
  }
}