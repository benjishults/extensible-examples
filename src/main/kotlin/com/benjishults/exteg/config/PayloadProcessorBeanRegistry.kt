package com.benjishults.exteg.config

import com.benjishults.exteg.http.Processor

open class PayloadProcessorBeanRegistry : AbstractBeanRegistry<Processor>() {
    override val suffix: String = "Processor"
}
