package io.vertx.example;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.Operation;
import io.vertx.ext.web.openapi.RouterBuilder;

public class HelloWorldEmbedded {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    RouterBuilder.create(vertx, "openapi.yml", event -> {
      if (event.succeeded()) {
        RouterBuilder builder = event.result();
        Operation operation = builder.operation("HelloWorld");
        operation.handler(event1 -> event1.response().end("Hello World!"));
        operation.failureHandler(event12 -> {
          event12.failure().printStackTrace();
          event12.response().end("FAIL");
        });
        Router router = builder.createRouter();

        vertx
          .createHttpServer()
          .requestHandler(router)
          .listen(8080, handler -> {
            if (handler.succeeded()) {
              System.out.println("http://localhost:8080/");
            } else {
              System.err.println("Failed to listen on port 8080");
            }
          });
      } else {
        event.cause().printStackTrace();
      }
    });
  }

}
