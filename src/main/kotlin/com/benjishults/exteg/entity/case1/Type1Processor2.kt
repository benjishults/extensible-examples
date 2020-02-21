package com.benjishults.exteg.entity.case1

import com.benjishults.exteg.ExtensibleProcessor
import com.benjishults.exteg.FeatureExecutor
import com.benjishults.exteg.Processor
import com.benjishults.exteg.entity.EntityDto

class Type1Processor2Builder {

    private val featureExecutors: MutableList<FeatureExecutor> = mutableListOf()

    fun registerListener(executor: FeatureExecutor) = this.also {
        featureExecutors.add(executor)
    }

    fun removeListener(executor: FeatureExecutor) = this.also {
        featureExecutors.remove(executor)
    }

    fun build() = Type1Processor2(featureExecutors)

}

class Type1Processor2(override val featureExecutors: List<FeatureExecutor>) : ExtensibleProcessor {
    override fun doMainProcessing(message: EntityDto) =
            "case 1 executed"
}
