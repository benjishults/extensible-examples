# extensible-examples

Sample code demonstrating some ideas that help build solutions that are extremely easy to extend.

## Problem

We are writing a simple rest service with just a `POST` endpoint.

The payload is JSON with two properties: `type` and `case`.

Payloads with different types need to be handled in quite different ways.

We know about two type values now but the business tells us to expect that other types will
be coming.

### Extra challenge

Solve this in a way that we can later add code to handle new types without editing any
existing code except, maybe, configuration code.

## Details

The two known types of entities: `type1` and `type2`.  The business gives us rules for
validation and processing of the two types of entities.

For the sake of keeping the details very simple, let's say we get this:

1. Validation: An entity of type `type1` must have a `case` value of `case1`.
1. Validation: An entity of type `type2` must have a `case` value of `case2`.
2. Processing: The processing of an entity of type `type1` should result in the return value of `case 1 executed`.
2. Processing: The processing of an entity of type `type2` should result in the return value of `case 2 executed`.
3. Any other type should result in an error.
4. Design in a way that anticipates more types being possible in the future.

We will write a server so that the following session will occur:

```bash
$ curl http://localhost:8989/comment -d '{"type":"mess","case":"case2"}' -i
HTTP/1.1 422 Unprocessable Entity: No Validator found for verb=post noun=mess.
content-length: 0

$ curl http://localhost:8989/comment -d '{"type":"type1","case":"case2"}' -i
HTTP/1.1 400 Bad Request: Invalid payload for type type1.
content-length: 0

$ curl http://localhost:8989/comment -d '{"type":"type2","case":"case1"}' -i
HTTP/1.1 400 Bad Request: Invalid payload for type type2.
content-length: 0

$ curl http://localhost:8989/comment -d '{"type":"type2","case":"case2"}' -i
HTTP/1.1 200 OK
content-length: 15

case 2 executed
$ curl http://localhost:8989/comment -d '{"type":"type1","case":"case1"}' -i
HTTP/1.1 200 OK
content-length: 15

case 1 executed
```
