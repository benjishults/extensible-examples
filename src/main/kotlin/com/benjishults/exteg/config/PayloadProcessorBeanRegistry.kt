package com.benjishults.exteg.config

import com.benjishults.exteg.Processor

open class PayloadProcessorBeanRegistry : MapBeanRegistry<Processor> {
    override val suffix: String = "Processor"
    override val map: MutableMap<String, Processor> = mutableMapOf()
}
