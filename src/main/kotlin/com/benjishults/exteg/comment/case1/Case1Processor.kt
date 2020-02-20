package com.benjishults.exteg.comment.case1

import com.benjishults.exteg.Processor
import io.vertx.core.json.JsonObject

object Case1Processor : Processor {
    override fun process(message: JsonObject) =
            "case 1 executed"
}
