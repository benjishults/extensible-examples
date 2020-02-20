package com.benjishults.exteg.comment.config

import com.benjishults.exteg.comment.case1.Case1Processor
import com.benjishults.exteg.comment.case2.Case2Processor
import com.benjishults.exteg.config.PayloadProcessorBeanRegistry

object ProcessorsBeanRegistry : PayloadProcessorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        put("postType1Processor", Case1Processor)
        put("postType2Processor", Case2Processor)
    }
}
