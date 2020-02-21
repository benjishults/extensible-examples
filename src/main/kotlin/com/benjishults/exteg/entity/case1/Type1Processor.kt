package com.benjishults.exteg.entity.case1

import com.benjishults.exteg.Processor
import io.vertx.core.json.JsonObject

object Type1Processor : Processor {
    override fun process(message: JsonObject) =
            "case 1 executed"
}
