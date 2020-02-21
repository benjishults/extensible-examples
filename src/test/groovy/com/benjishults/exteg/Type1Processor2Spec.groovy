package com.benjishults.exteg

import com.benjishults.exteg.entity.EntityDto
import com.benjishults.exteg.entity.case1.Type1Processor
import com.benjishults.exteg.entity.case1.Type1Processor2
import com.benjishults.exteg.entity.config.ProcessorsBeanRegistry2
import spock.lang.Specification
import spock.lang.Unroll

class Type1Processor2Spec extends Specification {

    private Type1Processor2 processor

    def setup() {
        processor = ProcessorsBeanRegistry2.INSTANCE.getBeanOrError("post", "type1") as Type1Processor2
    }

    @Unroll
    def 'type1 processing passing options: #options results in output: #result'() {
        given: 'a type1 payload with case: case1'
        EntityDto object = new EntityDto("type1", "case1", options)

        expect: 'processing produces required result'
        processor.process(object) == result

        where:
        options     | result
        []          | "case 1 executed\nfeature 1\nfeature 2"
        ["unknown"] | "case 1 executed\nfeature 1\nfeature 2"
        ["skipF2"]  | "case 1 executed\nfeature 1"
    }

}
