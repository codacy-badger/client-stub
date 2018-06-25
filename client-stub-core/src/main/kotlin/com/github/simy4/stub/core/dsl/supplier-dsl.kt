package com.github.simy4.stub.core.dsl

@StubDslMarker
open class SupplierDsl<A: Any>(default: A) {
    internal var value: A = default
        private set

    operator fun A.unaryPlus() {
        value = this
    }
}

internal fun <S: SupplierDsl<A>, A: Any> initSupplier(init: S.() -> Unit, dsl: S): A =
        dsl.init().let { dsl.value }
