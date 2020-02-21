package com.benjishults.exteg.entity.case1

import com.benjishults.exteg.Processor
import com.benjishults.exteg.entity.EntityDto

object Type1Processor : Processor {
    override fun process(message: EntityDto) =
            "case 1 executed"
}
