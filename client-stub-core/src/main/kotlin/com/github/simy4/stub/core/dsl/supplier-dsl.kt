package com.github.simy4.stub.core.dsl

open class SupplierDsl<A>(default: A): StubDsl<A> {
    final override var result: A = default
        private set

    operator fun A.unaryPlus() {
        result = this
    }
}

open class MultimapSupplierDsl<A>: SupplierDsl<MutableMap<String, MutableCollection<A>>>(mutableMapOf()) {
    operator fun Pair<String, A>.unaryPlus() {
        result.getOrPut(first) { mutableListOf() } += second
    }
}

open class BodySupplierDsl: SupplierDsl<Any?>(null)