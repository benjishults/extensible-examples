package com.benjishults.exteg

import com.benjishults.exteg.entity.case1.Type1Validator
import io.vertx.core.json.JsonObject
import spock.lang.Specification
import spock.lang.Subject

class Type1ValidatorSpec extends Specification {

    private Type1Validator validator

    def setup() {
        validator = Type1Validator.INSTANCE
    }

    def 'type1 positive validation'() {
        given: 'a type1 payload with case: case1'
        JsonObject object = new JsonObject('{"type":"type1","case":"case1"}')
        expect: 'the payload is valid'
        validator.validate(object)
    }

    def 'type1 negative validation'() {
        given: 'a type1 payload with case: case2'
        JsonObject object = new JsonObject('{"type":"type1","case":"case2"}')
        expect: 'the payload is not valid'
        !validator.validate(object)
    }

}
