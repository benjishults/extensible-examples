package com.benjishults.exteg

import com.benjishults.exteg.comment.config.ProcessorsBeanRegistry
import com.benjishults.exteg.comment.config.ValidatorsBeanRegistry
import com.benjishults.exteg.comment.http.CommentEndpointConfig
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions

fun main() {
    val vertx: Vertx = Vertx.vertx()
    vertx.createHttpServer(HttpServerOptions()
            .setHost("localhost")
            .setPort(8989)
    ).requestHandler { request ->
        CommentEndpointConfig(ValidatorsBeanRegistry, ProcessorsBeanRegistry)
                .router(vertx)
                .handle(request)
    }.listen()
}
