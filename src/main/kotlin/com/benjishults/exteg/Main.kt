package com.benjishults.exteg

import com.benjishults.exteg.config.Processors
import com.benjishults.exteg.http.CommentEndpointConfig
import com.benjishults.exteg.config.Validators
import io.vertx.core.Vertx

fun main() {
    val server = Vertx.vertx().createHttpServer()
    CommentEndpointConfig(Validators, Processors).configure(server)
    server.listen()
}

