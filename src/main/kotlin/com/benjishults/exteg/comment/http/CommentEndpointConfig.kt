package com.benjishults.exteg.comment.http

import com.benjishults.exteg.config.AbstractBeanRegistry
import com.benjishults.exteg.http.EndpointConfig
import com.benjishults.exteg.http.Processor
import com.benjishults.exteg.http.Validator
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

private val commentPath = "/comment"

class CommentEndpointConfig(
        private val validators: AbstractBeanRegistry<Validator>,
        private val processors: AbstractBeanRegistry<Processor>,
        private val path: String = commentPath
) : EndpointConfig {

    override fun addRoutes(router: Router) {
        router.route(path).handler(BodyHandler.create())
        router.post(path).handler { routingContext ->
            val obj = routingContext.bodyAsJson
            val type = obj.map["type"] as String
            try {
                val validator = validators.getBeanOrError("post", type)
                if (validator.validate(obj)) {
                    val processor = processors.getBeanOrError("post", type)
                    val result = processor.process(obj)
                    routingContext.response()
                            .end(result)
                } else {
                    routingContext.response()
                            .setStatusCode(400)
                            .setStatusMessage("Bad Request: Invalid payload for type ${type}.")
                            .end()
                }
            } catch (e: Exception) {
                routingContext.response()
                        .setStatusCode(422)
                        .setStatusMessage("Unprocessable Entity: ${e.message}")
                        .end()
            }
        }
    }

}
