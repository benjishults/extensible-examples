package com.benjishults.exteg.config

abstract class SimpleMapBeanRegistry<T> : BeanRegistry<T>, MutableMap<String, T> by mutableMapOf<String, T>()
