package com.benjishults.exteg

import io.vertx.core.json.JsonObject

interface Validator {
    fun validate(message: JsonObject): Boolean
}
