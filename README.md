# extensible-examples

Sample code demonstrating some ideas that help build solutions that are extremely easy to extend.

## Extensible code

We call code "extensible" if features can be added without editing any existing code except,
possibly, configuration code.

## Adding types

To add a new type, simply implement the `Processor` and `Validator` interfaces and add those beans
to the bean registries.  The bean names should follow this convention:

```
<HTTP method lowercase><type value capitalized><'Processor' or 'Validator'>
```

You should not need to edit any existing code (other than configuration code).

## Adding features to type1

To add a new feature to type1, simply implement the `FeatureExecutor` interface and register
an instance of your class during the configuration of the `Type1Processor`.

## Walkthroughs of problems and approach

[Initial problem solved with factory method, etc](FactoryMethodAndTemplatePatternsInPractice.md).

[Second problem solved with delegation event model, etc](DelegationEventModelInPractice.md).
