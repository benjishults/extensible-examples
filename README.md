# extensible-examples

Sample code demonstrating some ideas that help build solutions that are extremely easy to extend.

## Adding features

To add a new type, simply implement the `Processor` and `Validator` interfaces and add those beans
to the bean registries.

You should not need to edit any existing code (other than configuration code).
