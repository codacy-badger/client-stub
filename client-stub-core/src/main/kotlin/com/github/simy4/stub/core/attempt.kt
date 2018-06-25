package com.github.simy4.stub.core

sealed class Attempt<out A> {
    abstract fun <B> map(fmap: (A) -> B): Attempt<B>
    abstract fun <B> flatMap(fmap: (A) -> Attempt<B>): Attempt<B>
    abstract fun doOnSuccess(onSuccess: (A) -> Unit): Attempt<A>
    abstract fun doOnError(onError: (Throwable) -> Unit): Attempt<A>
    abstract fun run(): A

    companion object {
        operator fun <A> invoke(supplier: () -> A): Attempt<A> = try {
            Success(supplier())
        } catch (ex: Exception) {
            Failure(ex)
        }
    }
}

data class Success<A>(val value: A): Attempt<A>() {
    override fun <B> map(fmap: (A) -> B): Attempt<B> = try { Success(fmap(value)) } catch (t: Throwable) { Failure(t) }
    override fun <B> flatMap(fmap: (A) -> Attempt<B>): Attempt<B> = fmap(value)
    override fun doOnSuccess(onSuccess: (A) -> Unit): Attempt<A> {
        onSuccess(value)
        return this
    }
    override fun doOnError(onError: (Throwable) -> Unit): Attempt<A> = this
    override fun run(): A = value
}

data class Failure(val throwable: Throwable): Attempt<Nothing>() {
    override fun <B> map(fmap: (Nothing) -> B): Attempt<B> = this
    override fun <B> flatMap(fmap: (Nothing) -> Attempt<B>): Attempt<B> = this
    override fun doOnSuccess(onSuccess: (Nothing) -> Unit): Attempt<Nothing> = this
    override fun doOnError(onError: (Throwable) -> Unit): Attempt<Nothing> {
        onError(throwable)
        return this
    }
    override fun run(): Nothing = throw throwable
}
