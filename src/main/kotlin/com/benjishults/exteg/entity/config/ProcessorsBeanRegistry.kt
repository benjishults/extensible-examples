package com.benjishults.exteg.entity.config

import com.benjishults.exteg.config.PayloadProcessorBeanRegistry
import com.benjishults.exteg.entity.case1.Type1Processor
import com.benjishults.exteg.entity.case2.Type2Processor

object ProcessorsBeanRegistry : PayloadProcessorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        map["postType1Processor"] = Type1Processor
        map["postType2Processor"] = Type2Processor
    }
}
