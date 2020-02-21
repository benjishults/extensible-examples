package com.benjishults.exteg

import com.benjishults.exteg.entity.EntityDto

interface Validator {
    fun validate(message: EntityDto): Boolean
}
