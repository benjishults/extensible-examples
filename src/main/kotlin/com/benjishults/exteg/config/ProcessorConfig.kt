package com.benjishults.exteg.config

import com.benjishults.exteg.CommentProcessor
import com.benjishults.exteg.case1.Case1Processor
import com.benjishults.exteg.case2.Case2Processor

object Processors : HashMap<String, CommentProcessor>() {
    init {
        put("type1", Case1Processor)
        put("type2", Case2Processor)
    }
}
