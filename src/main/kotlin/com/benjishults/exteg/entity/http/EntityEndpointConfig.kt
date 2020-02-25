package com.benjishults.exteg.entity.http

import com.benjishults.exteg.Processor
import com.benjishults.exteg.Validator
import com.benjishults.exteg.config.MapBeanRegistry
import com.benjishults.exteg.entity.EntityDto
import com.benjishults.exteg.http.EndpointConfig
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

private val entityPath = "/entity"
private val mapper = jacksonObjectMapper()

class EntityEndpointConfig(
        private val validators: MapBeanRegistry<Validator>,
        private val processors: MapBeanRegistry<Processor>,
        private val path: String = entityPath
) : EndpointConfig {

    override fun addRoutes(router: Router) {
        router.route(path).handler(BodyHandler.create())
        router.post(path).handler { routingContext ->
            val entity = mapper.readValue(routingContext.bodyAsString, EntityDto::class.java)
            try {
                val validator = validators.getBeanOrError("post", entity.type)
                if (validator.validate(entity)) {
                    val processor = processors.getBeanOrError("post", entity.type)
                    val result = processor.process(entity)
                    routingContext.response()
                            .end(result)
                } else {
                    routingContext.response()
                            .setStatusCode(400)
                            .setStatusMessage("Bad Request: Invalid payload for type ${entity.type}.")
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
