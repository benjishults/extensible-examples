package com.benjishults.exteg.config

import com.benjishults.exteg.Validator

open class PayloadValidatorBeanRegistry : MapBeanRegistry<Validator> {
    override val suffix: String = "Validator"
    override val map: MutableMap<String, Validator> = mutableMapOf()
}
