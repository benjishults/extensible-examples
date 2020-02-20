package com.benjishults.exteg.http

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

interface EndpointConfig {

    fun router(vertx: Vertx): Router {
        val router: Router = Router.router(vertx)
        addRoutes(router)
        return router
    }

    fun addRoutes(router: Router)

}
