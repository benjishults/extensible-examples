package com.benjishults.exteg.entity.case2

import com.benjishults.exteg.Processor
import com.benjishults.exteg.entity.EntityDto

object Type2Processor : Processor {
    override fun process(message: EntityDto) =
        "Type 2 ${message.case} executed"
}
