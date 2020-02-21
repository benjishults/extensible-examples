package com.benjishults.exteg

import com.benjishults.exteg.entity.config.ProcessorsBeanRegistry
import com.benjishults.exteg.entity.config.ValidatorsBeanRegistry
import com.benjishults.exteg.entity.http.EntityEndpointConfig
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions

fun main() {
    val vertx: Vertx = Vertx.vertx()
    vertx.createHttpServer(HttpServerOptions()
            .setHost("localhost")
            .setPort(8989)
    ).requestHandler { request ->
        EntityEndpointConfig(ValidatorsBeanRegistry, ProcessorsBeanRegistry)
                .router(vertx)
                .handle(request)
    }.listen()
}
