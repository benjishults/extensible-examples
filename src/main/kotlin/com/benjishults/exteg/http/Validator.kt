package com.benjishults.exteg.http

import io.vertx.core.json.JsonObject

interface Validator {
    fun validate(message: JsonObject): Boolean
}
