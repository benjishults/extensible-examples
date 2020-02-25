package com.benjishults.exteg.entity.case1

import com.benjishults.exteg.FeatureExecutor
import com.benjishults.exteg.entity.EntityDto

class Feature1Executor : FeatureExecutor {

    override fun executeFeature(obj: EntityDto, value: String) =
            "$value\nfeature 1"

    override fun isApplicable(options: List<String>) = true

}
