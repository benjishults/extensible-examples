package com.benjishults.exteg.config

import com.benjishults.exteg.CommentValidator
import com.benjishults.exteg.case1.Case1Validator
import com.benjishults.exteg.case2.Case2Validator

object Validators : HashMap<String, CommentValidator>() {
    init {
        put("type1", Case1Validator)
        put("type2", Case2Validator)
    }
}
