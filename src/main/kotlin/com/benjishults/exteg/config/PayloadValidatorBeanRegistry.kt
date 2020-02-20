package com.benjishults.exteg.config

import com.benjishults.exteg.http.Validator

open class PayloadValidatorBeanRegistry : AbstractBeanRegistry<Validator>() {
    override val suffix: String = "Validator"
}
