package com.github.csandiego.pocaccount.wire

import com.squareup.wire.GrpcCall
import okio.Timeout

internal abstract class TestGrcpCall<S : Any, R : Any> : GrpcCall<S, R> {

    override val timeout: Timeout
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clone(): GrpcCall<S, R> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enqueue(request: S, callback: GrpcCall.Callback<S, R>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun execute(request: S): R {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun executeBlocking(request: S): R {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isCanceled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExecuted(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}