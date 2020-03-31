package com.benjishults.exteg

import com.benjishults.exteg.entity.EntityDto
import com.benjishults.exteg.entity.case2.Type2Validator
import spock.lang.Specification

class Type2ValidatorSpec extends Specification {

    private Type2Validator validator

    def setup() {
        validator = Type2Validator.INSTANCE
    }

    def 'type1 positive validation'() {
        given: 'a type2 payload with case: case2'
        EntityDto object = new EntityDto("type2", "case2", [])
        expect: 'the payload is valid'
        validator.validate(object)
    }

    def 'type1 negative validation'() {
        given: 'a type2 payload with case: case1'
        EntityDto object = new EntityDto("type2", "case1", [])
        expect: 'the payload is not valid'
        !validator.validate(object)
    }
}
