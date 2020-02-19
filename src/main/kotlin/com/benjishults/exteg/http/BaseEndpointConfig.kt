package com.benjishults.exteg.http

import com.benjishults.exteg.CommentProcessor
import com.benjishults.exteg.CommentValidator
import io.vertx.core.http.HttpServer
import io.vertx.core.json.JsonObject

abstract class BaseEndpointConfig(
        val validators: Map<String, CommentValidator>,
        val processors: Map<String, CommentProcessor>
) {

    fun configure(server: HttpServer) {
        server.requestHandler { request ->
            request.bodyHandler { event ->
                val obj = event.toJsonObject()
                val type = obj.map["type"] as String
                if (validate(type, obj)) {
                    process(type, obj)
                } else {
                    error("Invalid payload for type ${type}.")
                }
            }
        }
    }

    private fun process(type: String, obj: JsonObject) {
        processors.getOrElse(type) {
            error("No processor for type ${type}.")
        }.process(obj)
    }

    private fun validate(type: String, obj: JsonObject) =
            validators.getOrElse(type) {
                error("No validator for type ${type}.")
            }.validate(obj)
}
