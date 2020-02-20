package com.benjishults.exteg.comment.case1

import com.benjishults.exteg.http.Validator
import io.vertx.core.json.JsonObject

/**
 * Ensure the Case 1 conditions are satisfied
 */
object Case1Validator : Validator {
    override fun validate(message: JsonObject): Boolean {
        return message.map["case"] == "case1"
    }
}
