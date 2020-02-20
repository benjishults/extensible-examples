package com.benjishults.exteg.comment.case2

import com.benjishults.exteg.http.Validator
import io.vertx.core.json.JsonObject

/**
 * Ensure the Case 2 conditions are satisfied
 */
object Case2Validator : Validator {
    override fun validate(message: JsonObject): Boolean {
        return message.map["case"] == "case2"
    }
}
