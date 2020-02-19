package com.benjishults.exteg.case1

import com.benjishults.exteg.CommentValidator
import io.vertx.core.json.JsonObject

object Case1Validator : CommentValidator {
    override fun validate(message: JsonObject): Boolean {
        return message.map["case"] == "case1"
    }
}
