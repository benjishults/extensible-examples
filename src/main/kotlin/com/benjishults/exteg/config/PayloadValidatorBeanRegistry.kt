package com.benjishults.exteg.config

import com.benjishults.exteg.Validator

open class PayloadValidatorBeanRegistry : AbstractBeanRegistry<Validator>() {
    override val suffix: String = "Validator"
}
