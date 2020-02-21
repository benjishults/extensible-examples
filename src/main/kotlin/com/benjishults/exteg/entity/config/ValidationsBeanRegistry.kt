package com.benjishults.exteg.entity.config

import com.benjishults.exteg.entity.case1.Type1Validator
import com.benjishults.exteg.entity.case2.Type2Validator
import com.benjishults.exteg.config.PayloadValidatorBeanRegistry

object ValidatorsBeanRegistry : PayloadValidatorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        put("postType1Validator", Type1Validator)
        put("postType2Validator", Type2Validator)
    }
}
