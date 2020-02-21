package com.benjishults.exteg.entity.case2

import com.benjishults.exteg.Processor
import io.vertx.core.json.JsonObject

object Type2Processor : Processor {
    override fun process(message: JsonObject) =
            "case 2 executed"
}
