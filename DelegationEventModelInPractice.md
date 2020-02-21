# Problem

We learn from the business that `type1` entities need to have a new step added to their processing.

And another.

And we should expect more.

Also, some `type1` payloads will not want all of those steps to be executed based on the `options` value that will
now be part of the payload.

They want us to add the new features but be aware that more processing functionality may be requested later.

## Functional requirements

1. When processing an entity of thpe `type1`, we want to execute another step that adds a line to the output: `feature 1`.
1. When processing an entity of thpe `type1`, we want to execute another step that adds a line to the output: `feature 2`
unless the value of the `options` property contains `skipF2`.
2. Expect more, similar requirements to come specifying added, optional features.

## Non-Functional requirements

1. Design in a way that anticipates additional optional features..

# Solution

We will extend [what we did before](FactoryMethodAndTemplatePatternsInPractice.md).  We will edit the `Type1Processor` because
the requirements for its behavior have changed.  But, we will change it in a way that will not require us to edit again
when the new features come along.  (In fact, we will create a new Type1 Processor named `Type1Processor2` so that we can
keep both in the repo.)

## The Delegation Event Model

The Delegation Event Model is basically an implementation of a topic inside a single JVM.  But the
consumers and producers are just objects in memory.
It consists of

1. an event source (the producer)
2. a listener interface and any number of implementations (the consumers)
3. an event type (the message type)

The event source allows implementations of the listener interface to register interest in its events.
When the event occurs in the event source, it notifies all listeners.

In our case, 

1. the event source will be the processor
2. the listener interface will be called `FeatureExecutor` with an implementation for the two features we know about so far
3. the event type will be a pair: an `EntityDto` and the result of processing so far

### Listeners

Here is our listener interface:

```kotlin
interface FeatureExecutor {

    /**
     * Takes the entity and the result of processing so far and executes the feature on the two.
     */
    fun executeFeature(obj: EntityDto, value: String): String

    /**
     * Return [true] if this executor is applicable with the given options.
     */
    fun isApplicable(options: List<String>): Boolean

}
```

Usually, a listener interface has only one method but, just to show that the pattern is flexible, we've decided to add
the `isApplicable` function.  We've been told by the business that the determination of what features apply to a message
will always be based on the `options` property of the message so we only pass that value in.

Here are the two feature implementations:

```kotlin
class Feature1Executor : FeatureExecutor{

    override fun executeFeature(obj: EntityDto, value: String) = "$value\nfeature 1"

    override fun isApplicable(options: List<String>) = true

}
```

```kotlin
class Feature2Executor : FeatureExecutor {

    override fun executeFeature(obj: EntityDto, value: String) = "$value\nfeature 2"

    override fun isApplicable(options: List<String>) =
            "skipF2" !in options

}
```

Verify that these satisfy the business requirements.  (Of course, there are unit tests.)

### Event source

Our `Type1Processor2` is the event source.  Here is its code

```kotlin
class Type1Processor2(override val featureExecutors: List<FeatureExecutor>) : ExtensibleProcessor {
    override fun doMainProcessing(message: EntityDto) =
            "case 1 executed"
}
```

The only differences between `Type1Processor2` and `Type1Processor` are:

1. the interface they implement
2. the constructor now takes an argument
2. the name of the method they overload

Of course, the "magic" is happening in the interface it implements.
Here is the template interface for the event source:

```kotlin
interface ExtensibleProcessor : Processor {

    val featureExecutors: List<FeatureExecutor>

    /**
     * Runs all FeatureExecutors after doing the main processing.
     */
    override fun process(message: EntityDto): String {
        val seed = doMainProcessing(message)     // do main processing
        return onEntityProcessed(message, seed)  // execute all attached features
    }

    fun doMainProcessing(message: EntityDto): String

    private fun onEntityProcessed(message: EntityDto, seed: String) =
                                                                       // "fold" is like Java's "reduce" or Groovy's "inject"
            featureExecutors.fold(seed) { onGoingValue, executor ->    // onGoingValue is the result of the processing so far
                try {
                    if (executor.isApplicable(message.options))        // for each applicable feature executor
                        executor.executeFeature(message, onGoingValue) // execute the feature
                    else
                        onGoingValue                                   // otherwise, just return the value from the previous step
                } catch (e: Exception) {                               // we still want to execute the rest
                    println(e.message)
                    onGoingValue
                }
            }
}
```

Finally, we need a builder for our `Type1Processor2` because we want it to be immutable.

```kotlin
class Type1Processor2Builder {

    private val featureExecutors: MutableList<FeatureExecutor> = mutableListOf()

    fun registerListener(executor: FeatureExecutor) = this.also {
        featureExecutors.add(executor)
    }

    fun removeListener(executor: FeatureExecutor) = this.also {
        featureExecutors.remove(executor)
    }

    fun build() = Type1Processor2(featureExecutors)
}
```

### Wiring things together

The wiring happens in configuration code.  Here are the changes.  (Again, instead of actually changing code, we will
create a new class so that we can keep both examples in the same code repo.)

```kotlin
object ProcessorsBeanRegistry2 : PayloadProcessorBeanRegistry() {
    init {
        // NOTE just bind beans with these names to your DI context
        //      e.g., in Spring, you would simply create beans with these names
        put(
                "postType1Processor",
                Type1Processor2Builder()
                        .registerListener(Feature1Executor())
                        .registerListener(Feature2Executor())
                        .build())
        put("postType2Processor", Type2Processor)
    }
}
```

Notice the difference between this and the original `ProcessorsBeanRegistry`.  In this one, we use the builder
to construct a processor and we register both our our listeners with that Processor.

After editing the `Main.kt` code to use this `ProcessorsBeanRegistry2` instead of the old `ProcessorsBeanRegistry`,
the new code will become active.

Running it, we can see this:

```
$ curl http://localhost:8989/entity -d '{"type":"type1","case":"case1"}' -i
HTTP/1.1 200 OK
content-length: 35

case 1 executed
feature 1
feature 2%                                                                                                                                              
$ curl http://localhost:8989/entity -d '{"type":"type1","case":"case1","options":["skipF2"]}' -i
HTTP/1.1 200 OK
content-length: 25

case 1 executed
feature 1%                
```

Notice that passing the `skipF2` option disables that feature.

# Feature 3

What do we have to do when the business comes along and asks us to add a new feature?

1. Write a new implementation of `FeatureExecutor`
2. Register that new instance with the `Type1Processor2` in configuration code

# Things to notice about our code

The only `if` statement in this new code is for the applicability check.

We are using design patterns: Behavioral patterns (template, strategy), builder patterns (factory method).
Often, the adapter pattern is used in the Delegation Event Model in order to adapt between a business logic
class and the listener interface.

These patterns all take advantage polymorphism.

(In a functional language, this simplicity in design is also possible.  It's just done a bit differently.)
