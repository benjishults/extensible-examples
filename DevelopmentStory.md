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

We decide to use HTTP POST to allow clients to send us the entities.  The payload will look like this:

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

Also, we will just use a `JsonObject` class to deserialize the input rather than bringing in more dependencies.

On my machine, the web service starts up in less than half a second.

## Validators and Processors

Here are our two interfaces.

```kotlin
interface Processor {
    /**
     * @return the result of processing the message
     */
    fun process(message: JsonObject): String
}
```

```kotlin
interface Validator {
    fun validate(message: JsonObject): Boolean
}
```

We will write two implementations of each of those interfaces.
These implementations are in the `com.benjishults.exteg.entity.case1` and `com.benjishults.exteg.entity.case2` packages.
They are all the one-liners you would expect.
The implementations are named `Case1Validator`, `Case2Validator`, `Case1Processor`, and `Case2Processor`

## Dependency Injection

In order to keep things simple, I've written a very simple dependency injection "framework".
You can look at the details if you want, but here is what you need to know.

Validator beans are provided by this registry:

```kotlin
object ValidatorsBeanRegistry : PayloadValidatorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        put("postType1Validator", Case1Validator)
        put("postType2Validator", Case2Validator)
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
        put("postType1Processor", Case1Processor)
        put("postType2Processor", Case2Processor)
    }
}
```

You can see that I have beans in the DI context implementing each of the two types of processors.

The bean naming convention will be important later.

## Endpoint implementation

Here is the endpoint implementation:

```kotlin
private val entityPath = "/entity"

class EntityEndpointConfig(
        private val validators: AbstractBeanRegistry<Validator>,
        private val processors: AbstractBeanRegistry<Processor>,
        private val path: String = entityPath
) : EndpointConfig {

    override fun addRoutes(router: Router) {
        router.route(path).handler(BodyHandler.create())
        router.post(path).handler { routingContext ->
            val obj = routingContext.bodyAsJson
            val type = obj.map["type"] as String                            // get type from JSON payload
            try {
                val validator = validators.getBeanOrError("post", type)     // get validator from DI framework
                if (validator.validate(obj)) {                              // validate
                    val processor = processors.getBeanOrError("post", type) // get processor from DI framework
                    val result = processor.process(obj)                     // process
                    routingContext.response()
                            .end(result)
                } else {
                    routingContext.response()
                            .setStatusCode(400)
                            .setStatusMessage("Bad Request: Invalid payload for type ${type}.")
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

We get the payload as JSON and parse out the type.

From the type, we ask the DI framework for the validator and processor for that type.
(In a typical DI framework, this will come down to a hashtable lookup.)

## DI bean lookup

But how does the DI framework know which implementation of `Validator` or `Processor` to give us?
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

# Type 3

Now the product folks come along and tell you that they have a new type of message they want us to handle.

1. Validation: An entity of type `typo` must have a `case` value of `suit`.
2. Processing: The processing of an entity of type `typo` should result in the return value of `dressing applied`.

## Add the new feature without editing any existing code other than configuration code

The only existing code we have to edit is `ValidatorsBeanRegistry` and `ProcessorsBeanRegistry`.
I.e., configuration code that adds new beans to the context.

Other than that, we write a new, one-line implementation of `Processor` 
and a new, one-line implementation of `Validator`.

# Things to notice about our code

The only `if` statement in our code is for the validation check.  (And we really don't even need an if there.)

We are using design patterns: Behavioral patterns (template, strategy), builder patterns (factory method).

This is all possible because of polymorphism.

(In a functional language, this simplicity in design is also possible.  It's just done a bit differently.)
