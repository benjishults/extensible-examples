# Problem

Our clients want us to validate and process entities.

## Functional requirements

The two known types of entities: `type1` and `type2`.  The business gives us rules for
validation and processing of the two types of entities.

For the sake of keeping the details very simple, let's say we get these business requirements:

1. Validation: An entity of type `type1` must have a `case` value of `case1`.
1. Validation: An entity of type `type2` must have a `case` value of `case2`.
2. Processing: The processing of an entity of type `type1` should result in the return value of `case 1 executed`.
2. Processing: The processing of an entity of type `type2` should result in the return value of `case 2 executed`.
3. Any other type should result in an error.
4. All types of entities come in through the same channel.

## Non-Functional requirements

1. Design in a way that anticipates additional types,
with their own validation and processing requirements,
being possible in the future.

# Solution

We decide to use HTTP POST to allow clients to send us the entities.  The payload will [look like this](src/main/kotlin/com/benjishults/exteg/entity/EntityDto.kt):

```json5
{
  "type": "<type value>",
  "case": "<case value>"
}
```

We will write a server so that the following session would occur:

```
$ curl http://localhost:8989/entity -d '{"type":"mess","case":"case2"}' -i
HTTP/1.1 422 Unprocessable Entity: No Validator found for verb=post noun=mess.
content-length: 0

$ curl http://localhost:8989/entity -d '{"type":"type1","case":"case2"}' -i
HTTP/1.1 400 Bad Request: Invalid payload for type type1.
content-length: 0

$ curl http://localhost:8989/entity -d '{"type":"type2","case":"case1"}' -i
HTTP/1.1 400 Bad Request: Invalid payload for type type2.
content-length: 0

$ curl http://localhost:8989/entity -d '{"type":"type2","case":"case2"}' -i
HTTP/1.1 200 OK
content-length: 15

case 2 executed
$ curl http://localhost:8989/entity -d '{"type":"type1","case":"case1"}' -i
HTTP/1.1 200 OK
content-length: 15

case 1 executed
```

## Keep focus

The code is all in this repo but this walk-through will skip details about, e.g., the web-framework and DI implementation.

On my machine, the web service starts up in less than half a second.

## Validators and Processors

Here are our two interfaces.

```kotlin
interface Processor {
    /**
     * @return the result of processing the message
     */
    fun process(message: EntityDto): String
}
```

```kotlin
interface Validator {
    fun validate(message: EntityDto): Boolean
}
```

We will write two implementations of each of those interfaces.
These implementations are in 
the [`com.benjishults.exteg.entity.case1`](src/kotlin/com/benjishults/exteg/entity/case1)
and [`com.benjishults.exteg.entity.case2`](src/kotlin/com/benjishults/exteg/entity/case2)
packages.  They are all the one-liners you would expect.
The implementations are named
[`Type1Validator`](src/main/kotlin/com/benjishults/exteg/entity/case1/Type1Validator.kt),
[`Type2Validator`](src/main/kotlin/com/benjishults/exteg/entity/case2/Type2Validator.kt),
[`Type1Processor`](src/main/kotlin/com/benjishults/exteg/entity/case1/Type1Processor.kt), and
[`Type2Processor`](src/main/kotlin/com/benjishults/exteg/entity/case2/Type2Processor.kt).

## Dependency Injection

In order to keep things simple, I've written a very simple dependency injection "framework".
You can look at the details if you want, but here is what you need to know.

Validator beans are provided by this registry:

```kotlin
object ValidatorsBeanRegistry : PayloadValidatorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        put("postType1Validator", Type1Validator)
        put("postType2Validator", Type2Validator)
    }
}
```

You can see that I have beans in the DI context implementing each of the two types of validators.

The bean naming convention will be important later.

Processor beans are provided by this registry:

```kotlin
object ProcessorsBeanRegistry : PayloadProcessorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        put("postType1Processor", Type1Processor)
        put("postType2Processor", Type2Processor)
    }
}
```

You can see that I have beans in the DI context implementing each of the two types of processors.

The bean naming convention will be important later.

## Endpoint implementation

Here is the endpoint implementation:

```kotlin
private val entityPath = "/entity"
private val mapper = jacksonObjectMapper()

class EntityEndpointConfig(
        private val validators: AbstractBeanRegistry<Validator>,
        private val processors: AbstractBeanRegistry<Processor>,
        private val path: String = entityPath
) : EndpointConfig {

    override fun addRoutes(router: Router) {
        router.route(path).handler(BodyHandler.create())
        router.post(path).handler { routingContext ->
            val entity = mapper.readValue(routingContext.bodyAsString, EntityDto::class.java)
            try {
                val validator = validators.getBeanOrError("post", entity.type)     // get validator from DI framework     
                if (validator.validate(entity)) {                                  // validate                            
                    val processor = processors.getBeanOrError("post", entity.type) // get processor from DI framework
                    val result = processor.process(entity)                         // process                        
                    routingContext.response()                                      
                            .end(result)                                           
                } else {
                    routingContext.response()
                            .setStatusCode(400)
                            .setStatusMessage("Bad Request: Invalid payload for type ${entity.type}.")
                            .end()
                }
            } catch (e: Exception) {
                routingContext.response()
                        .setStatusCode(422)
                        .setStatusMessage("Unprocessable Entity: ${e.message}")
                        .end()
            }
        }
    }
}
```

What's happening there?

From the type, we ask the DI framework for the validator and processor for that type.

In a typical DI framework, this will come down to a hashtable lookup.

There is no reflection here.

## DI bean lookup

But how does the DI framework know which implementation of
[`Validator`](src/kotlin/com/benjishults/exteg/Validator.kt) or
[`Processor`](src/kotlin/com/benjishults/exteg/Processor.kt) to give us?
It's a simple naming convention for the bean names.  You can implement this easily in any major DI
framework.  Regardless of the DI framework, the code will resemble this:

```kotlin
abstract class AbstractBeanRegistry<T> : SimpleMapBeanRegistry<T>() {

    abstract val suffix: String

    fun getBeanOrError(verb: String, noun: String): T =
            getBeanOrElse(verb, noun) {
                error("No $suffix found for verb=$verb noun=$noun")
            }

    override fun getBeanOrElse(verb: String, noun: String, default: () -> T): T =
            getOrElse(buildString {
                append(verb.toLowerCase())
                append(noun.capitalize())
                append(suffix)
            }, default)
}
```

The bean naming convention is:

`<HTTP method in lower case><Capitalize the type><name of the interface I'm expecting>`

So, for example

`postType1Processor` is the name I gave one of my beans.
It would be found by the `getBeanOrElse` function above.

The code you would write in (e.g.) Spring would be almost identical.

In Spring, this comes down to a hashtable lookup.  There is no reflection here.

# Type 3

Now the product folks come along and tell you that they have a new type of message they want us to handle.

1. Validation: An entity of type `bigType` must have a `case` value of `large`.
2. Processing: The processing of an entity of type `bigType` should result in the return value of `largeness handled`.

## Add the new feature without editing any existing code other than configuration code

Depending on the DI framework you are using, you may not have to edit any existing code to make this work!

In our DI framework, the only existing code we have to edit is
[`ValidatorsBeanRegistry`](src/main/kotlin/com/benjishults/exteg/entity/config/ValidationsBeanRegistry.kt) and 
[`ProcessorsBeanRegistry`](src/main/kotlin/com/benjishults/exteg/entity/config/ProcessorsBeanRegistry.kt).
I.e., configuration code that adds new beans to the context.

Other than that, we write a new, one-line implementation of
[`Processor`](src/kotlin/com/benjishults/exteg/Processor.kt) 
and a new, one-line implementation of
[`Validator`](src/kotlin/com/benjishults/exteg/Validator.kt).

# Things to notice about our code

The only `if` statement in our code is for the validation check.  (And we really don't even need an if there.)

We are using design patterns: Behavioral patterns (template, strategy), builder patterns (factory method).

These patterns all use polymorphism.

(In a functional language, this simplicity in design is also possible.  It's just done a bit differently.)
