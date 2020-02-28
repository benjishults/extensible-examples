package com.benjishults.exteg.config

interface BeanRegistry<T> {
    fun getBeanOrElse(verb: String, type: String, default: () -> T): T
}
