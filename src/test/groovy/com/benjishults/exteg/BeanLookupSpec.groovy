package com.benjishults.exteg

import com.benjishults.exteg.config.AbstractBeanRegistry
import com.benjishults.exteg.config.BeanRegistry
import com.benjishults.exteg.entity.case1.Type1Processor
import com.benjishults.exteg.entity.case1.Type1Validator
import com.benjishults.exteg.entity.case2.Type2Processor
import com.benjishults.exteg.entity.case2.Type2Validator
import com.benjishults.exteg.entity.config.ProcessorsBeanRegistry
import com.benjishults.exteg.entity.config.ValidatorsBeanRegistry
import com.benjishults.exteg.entity.http.EntityEndpointConfig
import io.vertx.core.json.JsonObject
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class BeanLookupSpec extends Specification {

    private AbstractBeanRegistry<Validator> validatorBeanRegistry
    private AbstractBeanRegistry<Processor> processorBeanRegistry

    def setup() {
        validatorBeanRegistry = ValidatorsBeanRegistry.INSTANCE
        processorBeanRegistry = ProcessorsBeanRegistry.INSTANCE
    }

    def 'unknown type results in error'() {
        when: 'we look up a validator for an unknown type'
        validatorBeanRegistry.getBeanOrError("post", "")

        then: 'an IllegalStateException is thrown'
        thrown IllegalStateException
    }

    @Unroll
    def 'validator of type #validatorClass fetched for type #typeValue'() {
        when: 'we look up a validator for type'
        Validator validator = validatorBeanRegistry.getBeanOrError("post", typeValue)

        then: 'the correct type of validator is returned'
        validatorClass.isAssignableFrom(validator.class)

        where:
        typeValue | validatorClass
        'type1'   | Type1Validator
        'type2'   | Type2Validator
    }

    @Unroll
    def 'processor of type #processorClass fetched for type #typeValue'() {
        when: 'we look up a validator for type'
        Processor processor = processorBeanRegistry.getBeanOrError("post", typeValue)

        then: 'the correct type of validator is returned'
        processorClass.isAssignableFrom(processor.class)

        where:
        typeValue | processorClass
        'type1'   | Type1Processor
        'type2'   | Type2Processor
    }

}
