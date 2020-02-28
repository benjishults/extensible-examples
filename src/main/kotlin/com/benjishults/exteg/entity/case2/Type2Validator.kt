package com.benjishults.exteg.entity.case2

import com.benjishults.exteg.Validator
import com.benjishults.exteg.entity.EntityDto

/**
 * Ensure the Case 2 conditions are satisfied
 */
object Type2Validator : Validator {
    override fun validate(message: EntityDto): Boolean {
        return message.case.startsWith("case2")
    }
}
