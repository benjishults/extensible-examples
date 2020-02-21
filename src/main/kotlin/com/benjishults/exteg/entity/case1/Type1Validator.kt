package com.benjishults.exteg.entity.case1

import com.benjishults.exteg.Validator
import io.vertx.core.json.JsonObject

/**
 * Ensure the Case 1 conditions are satisfied
 */
object Type1Validator : Validator {
    override fun validate(message: JsonObject): Boolean {
        return message.map["case"] == "case1"
    }
}
