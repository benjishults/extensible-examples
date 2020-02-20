package com.benjishults.exteg.config

import com.benjishults.exteg.Processor

open class PayloadProcessorBeanRegistry : AbstractBeanRegistry<Processor>() {
    override val suffix: String = "Processor"
}
