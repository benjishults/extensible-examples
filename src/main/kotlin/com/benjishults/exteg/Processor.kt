package com.benjishults.exteg

import com.benjishults.exteg.entity.EntityDto

interface Processor {
    /**
     * @return the result of processing the message
     */
    fun process(message: EntityDto): String
}
