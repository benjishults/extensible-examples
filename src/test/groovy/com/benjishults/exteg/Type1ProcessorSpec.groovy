package com.benjishults.exteg


import com.benjishults.exteg.entity.case1.Type1Processor
import io.vertx.core.json.JsonObject
import spock.lang.Specification

class Type1ProcessorSpec extends Specification {

    private Type1Processor processor

    def setup() {
        processor = Type1Processor.INSTANCE
    }

    def 'type1 processing'() {
        given: 'a type1 payload with case: case1'
        JsonObject object = new JsonObject('{"type":"type1","case":"case1"}')
        expect: 'processing produces required result'
        processor.process(object) == "case 1 executed"
    }

}
