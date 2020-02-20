package com.benjishults.exteg.config

abstract class AbstractBeanRegistry<T> : SimpleMapBeanRegistry<T>() {

    abstract val suffix: String

    fun getBeanOrError(verb: String, noun: String): T =
            getBeanOrElse(verb, noun) {
                error("No $suffix found for verb=$verb noun=$noun")
            }

    override fun getBeanOrElse(verb: String, noun: String, default: () -> T): T =
            getOrElse(buildString {
                append(verb.toLowerCase())
                append(noun.capitalize())
                append(suffix)
            }, default)
}
