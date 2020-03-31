package com.benjishults.exteg.config

interface MapBeanRegistry<T> : BeanRegistry<T> {

    val map: Map<String, T>
    val suffix: String

    fun getBeanOrError(verb: String, type: String): T =
        getBeanOrElse(verb, type) {
            error("No $suffix found for verb=$verb type=$type")
        }

    override fun getBeanOrElse(verb: String, type: String, default: () -> T): T =
        map.getOrElse(buildString {
            append(verb.toLowerCase())
            append(type.capitalize())
            append(suffix)
        }, default)
}
