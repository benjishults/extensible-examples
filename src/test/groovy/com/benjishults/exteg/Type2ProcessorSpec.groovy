package com.benjishults.exteg

import com.benjishults.exteg.entity.EntityDto
import com.benjishults.exteg.entity.case2.Type2Processor
import io.vertx.core.json.JsonObject
import spock.lang.Specification

class Type2ProcessorSpec extends Specification {

    private Type2Processor processor

    def setup() {
        processor = Type2Processor.INSTANCE
    }

    def 'type1 processing'() {
        given: 'a type2 payload with case: case2'
        EntityDto object = new EntityDto("type2", "case2", [])
        expect: 'processing produces required result'
        processor.process(object) == "case 2 executed"
    }

}
