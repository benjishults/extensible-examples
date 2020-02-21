package com.benjishults.exteg


import com.benjishults.exteg.entity.case2.Type2Validator
import io.vertx.core.json.JsonObject
import spock.lang.Specification
import spock.lang.Subject

class Type2ValidatorSpec  extends Specification {

    private Type2Validator validator

    def setup() {
        validator = Type2Validator.INSTANCE
    }

    def 'type1 positive validation'() {
        given: 'a type2 payload with case: case2'
        JsonObject object = new JsonObject('{"type":"type2","case":"case2"}')
        expect: 'the payload is valid'
        validator.validate(object)
    }

    def 'type1 negative validation'() {
        given: 'a type2 payload with case: case1'
        JsonObject object = new JsonObject('{"type":"type2","case":"case1"}')
        expect: 'the payload is not valid'
        !validator.validate(object)
    }
}
