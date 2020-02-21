package com.benjishults.exteg.entity.case2

import com.benjishults.exteg.Validator
import io.vertx.core.json.JsonObject

/**
 * Ensure the Case 2 conditions are satisfied
 */
object Type2Validator : Validator {
    override fun validate(message: JsonObject): Boolean {
        return message.map["case"] == "case2"
    }
}
