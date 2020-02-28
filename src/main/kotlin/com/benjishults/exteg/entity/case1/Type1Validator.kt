package com.benjishults.exteg.entity.case1

import com.benjishults.exteg.Validator
import com.benjishults.exteg.entity.EntityDto

/**
 * Ensure the Case 1 conditions are satisfied
 */
object Type1Validator : Validator {
    override fun validate(message: EntityDto): Boolean {
        return message.case.startsWith("case1")
    }
}
