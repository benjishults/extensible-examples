package com.benjishults.exteg.case2

import com.benjishults.exteg.CommentProcessor
import io.vertx.core.json.JsonObject

object Case2Processor : CommentProcessor {
    override fun process(message: JsonObject) {
        println("case 2 executed")
    }
}
