package com.benjishults.exteg

import com.benjishults.exteg.entity.EntityDto

interface ExtensibleProcessor : Processor {

    val featureExecutors: List<FeatureExecutor>

    /**
     * Runs all FeatureExecutors after doing the main processing.
     */
    override fun process(message: EntityDto): String {
        val seed = doMainProcessing(message)
        return onEntityProcessed(message, seed)
    }

    fun doMainProcessing(message: EntityDto): String

    private fun onEntityProcessed(message: EntityDto, seed: String) =
            featureExecutors.fold(seed) { onGoingValue, executor ->
                try {
                    if (executor.isApplicable(message.options))
                        executor.executeFeature(message, onGoingValue)
                    else
                        onGoingValue
                } catch (e: Exception) {
                    println(e.message)
                    onGoingValue
                }
            }

}
