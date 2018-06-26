package com.github.simy4.stub.core.dsl

@StubDslMarker
open class SupplierDsl<A>(default: A) {
    internal var value: A = default
        private set

    operator fun A.unaryPlus() {
        value = this
    }
}

open class MultimapSupplierDsl<A>: SupplierDsl<MutableMap<String, MutableCollection<A>>>(mutableMapOf()) {
    operator fun Pair<String, A>.unaryPlus() {
        value.getOrPut(this.first) { mutableListOf() } += this.second
    }
}

open class BodySupplierDsl: SupplierDsl<Any?>(null)

internal fun <S: SupplierDsl<A>, A> initSupplier(init: S.() -> Unit, dsl: S): A =
        dsl.init().let { dsl.value }
