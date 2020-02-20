package com.benjishults.exteg.comment.config

import com.benjishults.exteg.comment.case1.Case1Validator
import com.benjishults.exteg.comment.case2.Case2Validator
import com.benjishults.exteg.config.PayloadValidatorBeanRegistry

object ValidatorsBeanRegistry : PayloadValidatorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        put("postType1Validator", Case1Validator)
        put("postType2Validator", Case2Validator)
    }
}
