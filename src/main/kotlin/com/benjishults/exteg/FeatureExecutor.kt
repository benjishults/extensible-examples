package com.benjishults.exteg

import com.benjishults.exteg.entity.EntityDto

interface FeatureExecutor {

    /**
     * Takes the entity and the result of processing so far and executes the feature on the two.
     */
    fun executeFeature(obj: EntityDto, value: String): String

    /**
     * Return [true] if this executor is applicable with the given options.
     */
    fun isApplicable(options: List<String>): Boolean

}
