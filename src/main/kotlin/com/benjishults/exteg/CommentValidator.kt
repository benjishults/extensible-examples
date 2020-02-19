package com.benjishults.exteg

import io.vertx.core.json.JsonObject

interface CommentValidator {
    fun validate(message: JsonObject): Boolean
}
