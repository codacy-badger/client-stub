# client-stub
[![Build Status](https://travis-ci.org/SimY4/client-stub.svg?branch=master)](https://travis-ci.org/SimY4/client-stub) 
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


Lightweight RPC stubbing library for integration tests

# Supported RPC clients

 - Jersey 1
 - Jersey 2

# Usage

Include an artifact with necessary model extension into your project:

```xml
<dependency>
    <groupId>com.github.simy4.stub</groupId>
    <artifactId>client-stub-jersey1</artifactId>
    <version>1.0.0</version>
</dependency>
```

Now create an instance of stubbed client via provided factory methods:

```kotlin
val stubHandler = jersey1.Jersey1StubHandler()
val client: Client = jersey1.ClientStub.create(stubHandler)
```

For stubbing use handler's DSL:

```kotlin
stubHandler.stub {
    request {
        method {+"GET"}
        path { matches("/foo/bar") }
    }
    response {
        code {+200}
        body {+"OK"}
    }
}
```
