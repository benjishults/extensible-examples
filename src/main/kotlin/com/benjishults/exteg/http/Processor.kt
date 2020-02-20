package com.benjishults.exteg.http

import io.vertx.core.json.JsonObject

interface Processor {
    /**
     * @return the result of processing the message
     */
    fun process(message: JsonObject): String
}
