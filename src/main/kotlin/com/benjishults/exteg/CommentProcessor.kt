package com.benjishults.exteg

import io.vertx.core.json.JsonObject

interface CommentProcessor {
    fun process(message: JsonObject)
}
