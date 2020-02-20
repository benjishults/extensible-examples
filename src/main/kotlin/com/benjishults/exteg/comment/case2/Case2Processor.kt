package com.benjishults.exteg.comment.case2

import com.benjishults.exteg.http.Processor
import io.vertx.core.json.JsonObject

object Case2Processor : Processor {
    override fun process(message: JsonObject) =
            "case 2 executed"
}
