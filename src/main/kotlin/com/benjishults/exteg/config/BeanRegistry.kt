package com.benjishults.exteg.config

interface BeanRegistry<T> {
    fun getBeanOrElse(verb: String, noun: String, default: () -> T): T
}
