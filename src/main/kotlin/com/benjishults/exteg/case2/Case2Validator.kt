package com.benjishults.exteg.case2

import com.benjishults.exteg.CommentValidator
import io.vertx.core.json.JsonObject

object Case2Validator : CommentValidator {
    override fun validate(message: JsonObject): Boolean {
        return message.map["case"] == "case2"
    }
}
