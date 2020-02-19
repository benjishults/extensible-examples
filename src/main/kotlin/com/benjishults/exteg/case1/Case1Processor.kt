package com.benjishults.exteg.case1

import com.benjishults.exteg.CommentProcessor
import io.vertx.core.json.JsonObject

object Case1Processor : CommentProcessor {
    override fun process(message: JsonObject) {
        println("case 1 executed")
    }
}
